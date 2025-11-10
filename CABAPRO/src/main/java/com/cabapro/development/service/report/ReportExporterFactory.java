package com.cabapro.development.service.report;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReportExporterFactory {

    private final Map<String, ReportExporter> byType;

    public ReportExporterFactory(Map<String, ReportExporter> exporters) {
        // Spring inyecta todas las implementaciones de ReportExporter en el Map: {beanName->impl}
        // Creamos un mapa por "type"
        this.byType = exporters.values().stream()
                .collect(java.util.stream.Collectors.toMap(
                        ReportExporter::getType,
                        e -> e,
                        (a, b) -> a));
    }

    public ReportExporter get(String type) {
        ReportExporter exp = byType.get(type);
        if (exp == null)
            throw new ReportException("Formato no soportado: " + type);
        return exp;
    }
}