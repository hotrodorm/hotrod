package websitegenerator.pdfmanual;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class PDFRenderer {

  private static final Logger log = Logger.getLogger(PDFRenderer.class);

  public static void renderPdf(final String content, final OutputStream os, final ResourceProvider provider)
      throws DocumentException, IOException {

    Document document = new Document(PageSize.LETTER);

    float topMargin = 72.0f * (0.50f + 0.125f);
    float bottomMargin = 72.0f;
    float leftMargin = 72.0f;
    float rightMargin = 72.0f;

    document.setMargins(leftMargin, rightMargin, topMargin, bottomMargin);

    FontFactory.defaultEmbedding = true;

    PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
    document.open();

    SimpleLetterPageRenderer pageEventRenderer = generate2(content, document, pdfWriter, provider);

    if (document.isOpen()) {
      document.close();
    }

    if (pageEventRenderer.getThrowable() != null) {
      throw new IOException("Could not generate PDF.", pageEventRenderer.getThrowable());
    }

  }

  private static SimpleLetterPageRenderer generate2(final String content, Document document, PdfWriter pdfWriter,
      final ResourceProvider provider) throws IOException {

    String FULL_ENCODER = "UTF-8";
    Charset charset = Charset.availableCharsets().get(FULL_ENCODER);
    if (charset == null) {
      throw new RuntimeException("Charset " + FULL_ENCODER + " is not supported. Cannot generate PDF.");
    }

    SimpleLetterPageRenderer pageEventRenderer = new SimpleLetterPageRenderer(provider);
    pdfWriter.setPageEvent(pageEventRenderer);

    XMLWorkerFontProvider fontProvider = getFontProvider();

    FontFactory.setFontImp(fontProvider);

    ByteArrayInputStream contentIs = new ByteArrayInputStream(content.getBytes());

    CssFilesImpl cssFiles = new CssFilesImpl();
    StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
    HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
    hpc.setAcceptUnknown(true).autoBookmark(true);
    // .setTagFactory(new ExtendedTagProcessorFactory());
    HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, pdfWriter));
    Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
    XMLWorker worker = new XMLWorker(pipeline, true);
    XMLParser p = new XMLParser(true, worker, charset);
    p.parse(contentIs, charset);

    return pageEventRenderer;

  }

  public static XMLWorkerFontProvider getFontProvider() {
    XMLWorkerFontProvider provider = new XMLWorkerFontProvider();
    // provider.register("fonts/open-symbol/opens___.ttf", "Open Symbol");
    // provider.register("fonts/nimbus-roman-no9-l/NimbusRomNo9L-Reg.otf",
    // "Nimbus Roman");
    return provider;
  }

}
