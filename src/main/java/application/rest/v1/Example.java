package application.rest.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Example {


  @RequestMapping(value = "v1/", method= RequestMethod.GET, produces = "application/json")
  //public @ResponseBody ResponseEntity<String> example() 
  public Map<String,String> example()
  {
	  
    //List<String> list = new ArrayList<>();
    //return a simple list of strings
    //list.add("Congratulations, your application is up and running");
    //return new ResponseEntity<String>(list.toString(), HttpStatus.OK);
    Map<String,String> result = new HashMap<>();
	result.put("message", "Congratulations, your application is up and running");
	return result;
  }


}
