package com.pichincha.service.service.impl;

import com.pichincha.service.service.JasperReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class JasperReportServiceImpl implements JasperReportService {

    @Override
    public byte[] getReportWithCollectionDataSource(String reportName, HashMap<String, Object> parameters, Collection collectionDataSource) throws JRException, IOException {

        InputStream jasperReport = getCompileReport(reportName);
        JRDataSource dataSource = new JRBeanCollectionDataSource(collectionDataSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return exportToPDF(jasperPrint);
    }

    @Override
    public byte[] getReportsWithCollectionDataSource(List<Map<String, Object>> reports) throws JRException, IOException {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        for (Map<String, Object> report : reports) {
            InputStream jasperReport = getCompileReport((String) report.get("reportName"));
            JRDataSource dataSource = new JRBeanCollectionDataSource((Collection) report.get("collectionDataSource"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    (HashMap<String, Object>) report.get("parameters"), dataSource);

            jasperPrints.add(jasperPrint);
        }

        return exportToPDF(jasperPrints.toArray(new JasperPrint[]{}));
    }

    private InputStream getCompileReport(String reportName) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("Reports/" + reportName + ".jasper");
        return classPathResource.getInputStream();

    }


    private byte[] exportToPDF(JasperPrint... jasperPrint) throws JRException, IOException {

        byte[] pdf;

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, Arrays.asList(jasperPrint));
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporter.exportReport();
            pdf = byteArrayOutputStream.toByteArray();

        }

        return pdf;
    }

}
