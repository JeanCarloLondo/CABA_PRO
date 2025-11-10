package com.cabapro.development.service.report;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.service.RankingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingReportService {

    private final RankingService rankingService;
    private final ReportExporterFactory factory;

    public RankingReportService(RankingService rankingService, ReportExporterFactory factory) {
        this.rankingService = rankingService;
        this.factory = factory;
    }

    /**
     * Genera el reporte en el formato indicado por type (por ejemplo "pdf" o "xlsx").
     */
    public byte[] generate(String type) {
        // Usar el método que sí existe en tu RankingService
        List<Ranking> data = rankingService.findAll();

        ReportExporter exporter = factory.get(type);
        return exporter.exportRankings(data);
    }
}