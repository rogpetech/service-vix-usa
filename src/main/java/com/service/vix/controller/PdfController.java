///* 
// * ===========================================================================
// * File Name PdfController.java
// * 
// * Created on 20-Jul-2023
// *
// * This code contains copyright information which is the proprietary property
// * of ServiceVix. No part of this code may be reproduced, stored or transmitted
// * in any form without the prior written permission of ServiceVix.
// *
// * Copyright (C) ServiceVix. 2023
// * All rights reserved.
// *
// * Modification history:
// * $Log: PdfController.java,v $
// * ===========================================================================
// */
// /**
// * Class Information
// * @author rajeshy3 - Chetu
// * @version 1.0 - 20-Jul-2023
// */
// package com.service.vix.controller;
// import java.util.HashMap;
// import java.util.Map;
// 
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
//
//import com.service.vix.utility.PDFGenerator;
//
//import jakarta.servlet.http.HttpServletResponse;
//
// @Controller
// public class PdfController {
//
//     @GetMapping("/pdf")
//     public String showPdfForm() {
//         return "pdf_form";
//     }
//
//     @PostMapping("/generatePdf")
//     public void generatePdf(@RequestParam("name") String name, HttpServletResponse response) {
//         try {
//             // Prepare data for the PDF template
//             Map<String, Object> data = new HashMap<>();
//             data.put("name", name);
//             // Add more dynamic data here if needed
//
//             // Generate the PDF
//             byte[] pdfBytes = new PDFGenerator().generatePDF("pdf_template", data);
//
//             // Set response headers
//             response.setContentType("application/pdf");
//             response.setHeader("Content-Disposition", "inline; filename=generated_pdf.pdf");
//
//             // Write PDF bytes to the response
//             response.getOutputStream().write(pdfBytes);
//             response.flushBuffer();
//         } catch (Exception e) {
//             // Handle exceptions and show an error page or message
//             e.printStackTrace();
//         }
//     }
// }
//
