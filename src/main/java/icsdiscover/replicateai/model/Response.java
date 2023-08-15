package icsdiscover.replicateai.model;

record Urls (String get, String cancel) {	
}

record Metrics(double predict_time) {
}

public record Response(String id, String version, Urls urls, String created_at, String started_at, String completed_at, String source, String error, Input input, String logs, Metrics metrics, String output, String status) {
}