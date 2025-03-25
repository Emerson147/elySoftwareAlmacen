package com.emersondev.servicelmpl;

import com.emersondev.JWT.JwtFilter;
import com.emersondev.POJO.Bill;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.dao.BillDao;
import com.emersondev.service.BillService;
import com.emersondev.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

  @Autowired
  BillDao billDao;

  @Autowired
  JwtFilter jwtFilter;

  @Override
  public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
    {
      log.info("Insert generateReport");
      try {
        String filename;
        if (validateResquestMap(requestMap)) {
          if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
            filename = (String) requestMap.get("uuid");
          } else {
            filename = CafeUtils.getUUID();
            requestMap.put("uuid", filename);
            insertBill(requestMap);
          }
          // print user data (name , email m contactNumber , ...)
          String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " + requestMap.get("contactNumber") +
                  "\n" + "Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod");
          Document document = new Document();
          PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + filename + ".pdf"));
          document.open();
          setRectaangleInPdf(document);

          // print pdf Header
          Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
          chunk.setAlignment(Element.ALIGN_CENTER);
          document.add(chunk);


          Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
          document.add(paragraph);

          // Create table in pdf to print data
          PdfPTable table = new PdfPTable(5);
          table.setWidthPercentage(100);
          addTableHeader(table);


          // Print table data
          JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
          for (int i = 0; i < jsonArray.length(); i++) {
            String jsonString = jsonArray.getJSONObject(i).toString();
            addRows(table, CafeUtils.getMapFromJson(jsonString));
          }

          document.add(table);

          // print pdf Footer
          Paragraph footer = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n"
                  + "Thank you for visiting our website.", getFont("Data"));
          document.add(footer);
          document.close();
          return new ResponseEntity<>("{\"uuid\":\"" + filename + "\"}", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void addRows(PdfPTable table, Map<String, Object> data) {
    log.info("Inside addRows");
    table.addCell((String) data.get("name"));
    table.addCell((String) data.get("category"));
    table.addCell((String) data.get("quantity"));
    table.addCell(Double.toString((Double) data.get("price")));
    table.addCell(Double.toString((Double) data.get("total")));
  }

  private void addTableHeader(PdfPTable table) {
    Stream.of("Producto", "Categoría", "Cantidad", "P. Unitario", "Subtotal")
            .forEach(headerTitle -> {
              PdfPCell header = new PdfPCell();
              header.setBackgroundColor(new BaseColor(44, 62, 80)); // Gris oscuro
              header.setPhrase(new Phrase(headerTitle, getFont("HeaderTable")));
              header.setPadding(8);
              header.setHorizontalAlignment(Element.ALIGN_CENTER);
              table.addCell(header);
            });
  }

  private void setRectaangleInPdf(Document document) throws DocumentException {
    log.info("Inside setRectangleInPdf");
    Rectangle rectangle = new Rectangle(577, 825, 18, 15);
    rectangle.enableBorderSide(1);
    rectangle.enableBorderSide(2);
    rectangle.enableBorderSide(4);
    rectangle.enableBorderSide(8);
    rectangle.setBorderColor(BaseColor.BLACK);
    rectangle.setBorderWidth(1);
    document.add(rectangle);
  }
  
  private void insertBill(Map<String, Object> requestMap) {
    try {
      Bill bill = new Bill();
      bill.setUuid((String) requestMap.get("uuid"));
      bill.setName((String) requestMap.get("name"));
      bill.setEmail((String) requestMap.get("email"));
      bill.setContactNumber((String) requestMap.get("contactNumber"));
      bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
      bill.setTotal(Integer.valueOf(Integer.parseInt((String) requestMap.get("totalAmount"))));
      bill.setProductDetails((String) requestMap.get("productDetails"));
      bill.setCreatedBy(jwtFilter.getCurrentUsername());
      billDao.save(bill);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private boolean validateResquestMap(Map<String, Object> requestMap) {
    return requestMap.containsKey("name") &&
            requestMap.containsKey("contactNumber") &&
            requestMap.containsKey("email") &&
            requestMap.containsKey("paymentMethod") &&
            requestMap.containsKey("productDetails") &&
            requestMap.containsKey("totalAmount");
  }

  private Font getFont(String type) {
    log.info("Inside getFont");

    BaseColor primaryColor = new BaseColor(63, 169, 219); // Blue
    switch (type) {
      case "Header":
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, primaryColor);
      case "HeaderTable":
        return FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);
      case "Data":
        return FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
      case "TotalLabel":
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.DARK_GRAY);
      case "TotalValue":
        return FontFactory.getFont(FontFactory.HELVETICA, 10, new BaseColor(46, 204, 113)); // Grenn
      case "Footer":
        Font font = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY);
        font.setColor(BaseColor.GRAY);
        return font;
      default:
        return new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    }
  }

  @Override
  public ResponseEntity<List<Bill>> getBills() {
    List<Bill> list = new ArrayList<>();
    if (jwtFilter.isAdmin()) {
      list = billDao.getAllBills();
    } else {
      list = billDao.getBillsByUserName(jwtFilter.getCurrentUsername());
    }
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
    log.info("Inside getPdf : requestMap {}", requestMap);

    try {
      byte[] byteArray = new byte[0];
      if (!requestMap.containsKey("uuid") && validateResquestMap(requestMap)) {
        return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
      }
      String filepath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";

      if (CafeUtils.isFileExist(filepath)) {
        byteArray = getByteArray(filepath);
        return new ResponseEntity<>(byteArray, HttpStatus.OK);
      } else {
        requestMap.put("isGenerate", Optional.of(false));
        generateReport(requestMap);
        byteArray = getByteArray(filepath);
        return new ResponseEntity<>(byteArray, HttpStatus.OK);
      }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
  }


  private byte[] getByteArray(String filepath) throws Exception {
    File initalFile = new File(filepath);
    InputStream targetSteam = new FileInputStream(initalFile);
    byte[] byteArray = IOUtils.toByteArray(targetSteam);
    targetSteam.close();
    return byteArray;
  }


  @Override
  public ResponseEntity<String> deletePdf(Integer id) {
    try {
      if (jwtFilter.isAdmin()) {
        Optional optional = billDao.findById(id);
        if (!optional.isEmpty()) {
          billDao.deleteById(id);

          return CafeUtils.getResponseEntity("Bill is deleted successfully", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Bill id doesn't exist", HttpStatus.OK);
      } else {
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}