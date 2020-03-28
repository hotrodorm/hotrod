package websitegenerator.pdfmanual;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class SimpleLetterPageRenderer extends PdfPageEventHelper {

  private static final Logger log = Logger.getLogger(SimpleLetterPageRenderer.class);

  private ResourceProvider provider;
  private Throwable throwable;

  public SimpleLetterPageRenderer(final ResourceProvider provider) {
    super();
    this.provider = provider;

    this.throwable = null;
  }

  public void onEndPage(final PdfWriter writer, final Document document) {

    // footers

    // Font of = FontFactory.getFont("Times", 12);
    // PdfContentByte direct = writer.getDirectContent();
    //
    // ColumnText.showTextAligned(direct, Element.ALIGN_LEFT, //
    // new Phrase("OMB Control No. 1240-0002", of), 72f, 47, 0);
    // ColumnText.showTextAligned(direct, Element.ALIGN_LEFT, //
    // new Phrase("Expiration Date: 03/31/2020", of), 72f, 37, 0);
    //
    // ColumnText.showTextAligned(direct, Element.ALIGN_RIGHT, //
    // new Phrase("EE-11A", of), 530f, 47, 0);
    // ColumnText.showTextAligned(direct, Element.ALIGN_RIGHT, //
    // new Phrase("November 2016", of), 530f, 37, 0);

    // Page Number
    PdfContentByte direct = writer.getDirectContent();
    Font f = FontFactory.getFont("Times", 10);
    Phrase line1 = new Phrase("" + document.getPageNumber(), f);
    ColumnText.showTextAligned(direct, Element.ALIGN_LEFT, line1, document.getPageSize().getWidth() / 2.0f, 23, 0);

  }

  private Image loadImage(final String resourcePath) throws BadElementException, MalformedURLException, IOException {
    InputStream is = null;
    ByteArrayOutputStream os = null;
    try {
      is = this.provider.getResourceAsStream(resourcePath);
      if (is == null) {
        this.throwable = new IOException("Could not load image resource at '" + resourcePath + "'.");
        return null;
      }
      os = new ByteArrayOutputStream();

      byte[] buffer = new byte[16 * 1024];
      int read;
      while ((read = is.read(buffer)) != -1) {
        os.write(buffer, 0, read);
      }

      Image image = Image.getInstance(os.toByteArray());
      return image;
    } finally {
      if (is != null) {
        try {
          is.close();
        } finally {
          if (os != null) {
            os.close();
          }
        }
      }
    }
  }

  public Throwable getThrowable() {
    return throwable;
  }

}