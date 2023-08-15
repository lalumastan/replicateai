package icsdiscover.replicateai.model;

public record Request(String version, Input input) {
	
	private final static String REPLICATE_PREDICT_BABY = "2c228c4d2266c2a03fee359e7d1dd7cb20838e9d68500d18749e4213f6c6b97d";
	
	public Request(Input input) {
		this(REPLICATE_PREDICT_BABY, input);
	}
}
