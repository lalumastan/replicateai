package icsdiscover.replicateai;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import icsdiscover.replicateai.model.FormBean;
import icsdiscover.replicateai.model.Response;
import jakarta.servlet.http.HttpSession;

@SessionAttributes({ "currentId" })
@Controller
public class ApplicationController {
	private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

	private final static Locale locale = Locale.getDefault();

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/")
	public String index(FormBean formBean, Model model, SessionStatus status) {
		model.addAttribute("formBean", formBean);
		status.setComplete();

		return "index";
	}

	@ResponseBody
	@PostMapping("/predict")
	public String predict(@ModelAttribute("formBean") FormBean formBean, Model model, HttpSession session) {
		log.info("Getting prediction from Replicateai");
		String result = "Fatal Error";
		try {
			Response response = applicationService.predict(formBean);			
			String currentId = response.id();
			result = response.status();
			model.addAttribute("currentId", currentId);
		} catch (Exception e) {
			e.printStackTrace();
			result = messageSource.getMessage("apiCallfailed", new String[] { e.getMessage() }, locale);
		}

		return result;
	}
	
	@ResponseBody
	@GetMapping("/status")
	public String status(HttpSession session) {
		log.info("Getting status from Replicateai");
		String result = "Fatal Error";
		try {			
			result = applicationService.status((String) session.getAttribute("currentId"));			
		} catch (Exception e) {
			e.printStackTrace();
			result = messageSource.getMessage("apiCallfailed", new String[] { e.getMessage() }, locale);
		}

		return result;
	}	
}
