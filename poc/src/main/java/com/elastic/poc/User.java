package com.elastic.poc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {

	  private String userId;
	  private String name;
	  private Date creationDate = new Date();
	  private Map<String, String> userSettings = new HashMap<>();
}
