package org.hotrod.torcs.rankings;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXRankingWriter {

  public static void writeTo(final Ranking ranking, final OutputStream os) throws IOException {

    int line;
    int col;

    try (Workbook workbook = new XSSFWorkbook()) {

      // Sheet

      Sheet sheet = workbook.createSheet("Torcs Ranking - " + ranking.getTitle());
      line = 0;

      // Styles

      CellStyle nameStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        nameStyle.setFont(font);
      }

      CellStyle headerLeftStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        headerLeftStyle.setFont(font);
      }

      CellStyle headerCenterStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        headerCenterStyle.setFont(font);
        headerCenterStyle.setAlignment(HorizontalAlignment.CENTER);
      }

      CellStyle headerRightStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        headerRightStyle.setFont(font);
        headerRightStyle.setAlignment(HorizontalAlignment.RIGHT);
      }

      CellStyle dataLeftStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
//        font.setBold(false);
        dataLeftStyle.setFont(font);
      }

      CellStyle dataCenterStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
//        font.setBold(false);
        dataCenterStyle.setFont(font);
        dataCenterStyle.setAlignment(HorizontalAlignment.CENTER);
      }

      CellStyle dataRightStyle = workbook.createCellStyle();
      {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
//        font.setBold(false);
        dataRightStyle.setFont(font);
        dataRightStyle.setAlignment(HorizontalAlignment.RIGHT);
      }

      // Title

      {
        Row r = sheet.createRow(line++);
        col = 0;
        col = addCell(r, col, nameStyle, "Torcs Ranking - " + ranking.getTitle());
      }
      // Blank line

      line++;

      // Header

      {
        Row r = sheet.createRow(line++);
        col = 0;
        col = addCell(r, col, headerLeftStyle, "Query");
        col = addCell(r, col, headerRightStyle, "Tmin [ms]");
        col = addCell(r, col, headerRightStyle, "Tmax [ms]");
        col = addCell(r, col, headerRightStyle, "Tavg [ms]");
        col = addCell(r, col, headerRightStyle, "Tstddev [ms]");
        col = addCell(r, col, headerRightStyle, "TET [ms]");
        col = addCell(r, col, headerRightStyle, "#total");
        col = addCell(r, col, headerRightStyle, "#fail");
        col = addCell(r, col, headerRightStyle, "#ok");
        col = addCell(r, col, headerCenterStyle, "first exec");
        col = addCell(r, col, headerCenterStyle, "last exec");
        col = addCell(r, col, headerCenterStyle, "last fail");
      }

      // Entries

      DecimalFormat df = new DecimalFormat("0");
      DateTimeFormatter tsf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

      for (RankingEntry re : ranking.getEntries()) {
        {
          Row r = sheet.createRow(line++);
          col = 0;
          col = addCell(r, col, dataLeftStyle, re.getCompactSQL());
          col = addCell(r, col, dataRightStyle, df.format(re.getMinTime()));
          col = addCell(r, col, dataRightStyle, df.format(re.getMaxTime()));
          col = addCell(r, col, dataRightStyle, df.format(re.getAverageTime()));
          col = addCell(r, col, dataRightStyle, df.format(re.getTimeStandardDeviation()));
          col = addCell(r, col, dataRightStyle, df.format(re.getTotalElapsedTime()));
          col = addCell(r, col, dataRightStyle, df.format(re.getExecutions()));
          col = addCell(r, col, dataRightStyle, df.format(re.getErrors()));
          col = addCell(r, col, dataRightStyle, df.format(re.getExecutions() - re.getErrors()));
          col = addCell(r, col, dataCenterStyle,
              re.getFirstExecutionAt() == 0 ? "N/A" : format(re.getFirstExecutionAt(), tsf));
          col = addCell(r, col, dataCenterStyle,
              re.getLastExecutionAt() == 0 ? "N/A" : format(re.getLastExecutionAt(), tsf));
          col = addCell(r, col, dataCenterStyle,
              re.getLastExceptionTimestamp() == 0 ? "N/A" : format(re.getLastExceptionTimestamp(), tsf));
        }
      }

      // Auto width

      try {
        for (int i = 1; i < 12; i++) {
          sheet.autoSizeColumn(i);
        }
      } catch (Exception e) {
        // Ignore. Some platforms (e.g. Android) may crash on the absence of fonts
        // and/or other resources)
      }

      // write XLSX

      workbook.write(os);

    }

  }

  private static String format(final long t, final DateTimeFormatter f) {
    LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
    return f.format(dt);
  }

  private static int addCell(final Row r, final int col, final CellStyle style, final String content) {
    Cell c = r.createCell(col);
    c.setCellValue(content);
    if (style != null) {
      c.setCellStyle(style);
    }
    return col + 1;
  }

}
