package manual;

import java.io.FileOutputStream;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class LineChart {

  static void createXSSFSheetWithLineChart(XSSFSheet sheet, String chartTitle, String catAxisTitle, String yAxisTitle,
      XSSFCell[] headers, CellRangeAddress dataRange, XSSFClientAnchor anchor) {

    XSSFDrawing drawing = sheet.createDrawingPatriarch();

    XSSFChart chart = drawing.createChart(anchor);
    chart.setTitleText(chartTitle);
    chart.setTitleOverlay(false);

    XDDFChartLegend legend = chart.getOrAddLegend();
    legend.setPosition(LegendPosition.TOP_RIGHT);

    XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
    bottomAxis.setTitle(catAxisTitle);
    XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
    leftAxis.setTitle(yAxisTitle);

    XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(
        dataRange.getFirstRow(), dataRange.getLastRow(), dataRange.getFirstColumn(), dataRange.getFirstColumn()));
    XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
        new CellRangeAddress(dataRange.getFirstRow(), dataRange.getLastRow(), dataRange.getFirstColumn() + 1,
            dataRange.getFirstColumn() + 1));
    XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
        new CellRangeAddress(dataRange.getFirstRow(), dataRange.getLastRow(), dataRange.getFirstColumn() + 2,
            dataRange.getFirstColumn() + 2));

    XDDFChartData data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
    XDDFChartData.Series series1 = data.addSeries(xs, ys1);
    series1.setTitle(headers[0].getStringCellValue(), new CellReference(headers[0]));
    ((XDDFLineChartData.Series) series1).setSmooth(false);
    ((XDDFLineChartData.Series) series1).setMarkerStyle(MarkerStyle.STAR);

    XDDFChartData.Series series2 = data.addSeries(xs, ys2);
    series2.setTitle(headers[1].getStringCellValue(), new CellReference(headers[1]));
    ((XDDFLineChartData.Series) series2).setSmooth(true);
    ((XDDFLineChartData.Series) series2).setMarkerSize((short) 6);
    ((XDDFLineChartData.Series) series2).setMarkerStyle(MarkerStyle.SQUARE);

    chart.plot(data);
  }

  static void streamDataIntoSXSSFWorkbook(SXSSFSheet sheet, int dataStartRow, int dataSize) {
    for (int r = dataStartRow; r < dataStartRow + dataSize; r++) {
      SXSSFRow row = sheet.createRow(r);
      SXSSFCell cell = row.createCell(0);
      cell.setCellValue("Per" + r);
      double d = new java.util.Random().nextDouble() * 1000;
      cell = row.createCell(1);
      cell.setCellValue(d);
      cell = row.createCell(2);
      cell.setCellValue(d / (new java.util.Random().nextDouble() + 1));
    }
  }

  public static void main(String[] args) throws Exception {

    final int DATA_START_ROW = 1;
    final int DATA_SIZE = 100;

    XSSFWorkbook wb = new XSSFWorkbook();
    XSSFSheet sheet = wb.createSheet();
    XSSFRow row = sheet.createRow(0);
    XSSFCell cell = row.createCell(0);
    cell.setCellValue("Period");
    XSSFCell[] headers = new XSSFCell[2];
    cell = row.createCell(1);
    cell.setCellValue("Total Paid");
    headers[0] = cell;
    cell = row.createCell(2);
    cell.setCellValue("Allowed");
    headers[1] = cell;

    createXSSFSheetWithLineChart(sheet, "Trend (Claim Type)", "Period", "Cost", headers,
        new CellRangeAddress(DATA_START_ROW, DATA_START_ROW + DATA_SIZE - 1, 0, 2),
        new XSSFClientAnchor(0, 0, 0, 0, 3, 1, 20, DATA_START_ROW + 20));

    SXSSFWorkbook sWb = new SXSSFWorkbook(wb);
    SXSSFSheet sSheet = sWb.getSheetAt(0);
    streamDataIntoSXSSFWorkbook(sSheet, DATA_START_ROW, DATA_SIZE);

    FileOutputStream fileOut = new FileOutputStream("chart-1.xlsx");
    sWb.write(fileOut);
    fileOut.close();
    sWb.dispose();
  }
}