package icsdiscover.replicateai;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import icsdiscover.replicateai.model.Input;
import icsdiscover.replicateai.model.FormBean;
import icsdiscover.replicateai.model.Request;
import icsdiscover.replicateai.model.Response;

@Service
public class ApplicationService {

	private static final String API_URL = "https://api.replicate.com/v1/predictions";

	// Set REPLICATE_API_TOKEN in the system environment variable
	private static final String KEY = System.getenv("REPLICATE_API_TOKEN");

	private static final ObjectMapper OM = new ObjectMapper();
	
	private String convertToBase64String(MultipartFile file) {
		String base64Image = null;
		
		try {
            byte[] imageBytes = file.getBytes();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return "data:image/png;base64," + base64Image;
	}

	public Response predict(FormBean formBean) throws Exception {
		HttpMethod httpMethod = HttpMethod.POST;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Token " + KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);		

		String jsonString = OM.writeValueAsString(new Request(new Input(convertToBase64String(formBean.getFather()), convertToBase64String(formBean.getMother()), formBean.getGender())));
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Response> responseEntity = restTemplate.exchange(API_URL, httpMethod, entity, Response.class);
		
		return responseEntity.getBody();
	}
	
	public String status(String requestPath) throws Exception {
		HttpMethod httpMethod = HttpMethod.GET;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Token " + KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);		

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Response> responseEntity = restTemplate.exchange(API_URL + "/" + requestPath, httpMethod, entity, Response.class);

		Response response = responseEntity.getBody();

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
		   String status = response.status();
		   return "succeeded".equals(status) ? response.output(): status;
		}

		return null;
	}	
    
}
