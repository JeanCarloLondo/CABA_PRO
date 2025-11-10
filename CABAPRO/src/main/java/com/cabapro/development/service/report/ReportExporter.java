package com.cabapro.development.service.report;

import com.cabapro.development.model.Ranking;
import java.util.List;

public interface ReportExporter {
    /** "pdf" | "xlsx" */
    String getType();

    /** Genera un binario (PDF o XLSX) con los rankings. */
    byte[] exportRankings(List<Ranking> data) throws ReportException;
}