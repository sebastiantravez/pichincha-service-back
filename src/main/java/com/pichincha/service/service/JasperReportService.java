package com.pichincha.service.service;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JasperReportService {

    byte[] getReportWithCollectionDataSource(String reportName, HashMap<String, Object> parameters, Collection collectionDataSource) throws JRException, IOException;

    byte[] getReportsWithCollectionDataSource(List<Map<String, Object>> reports) throws JRException, IOException;

}
