package org.gitistics.controllers;

import javax.inject.Inject;

import org.gitistics.outlier.OutlierHandler;
import org.gitistics.outlier.OutlierParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/outliers")
public class OutliersController {

	@Inject
	private OutlierHandler handler;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Iterable list(OutlierParam param) throws Exception {
		return handler.outliers(param);
	}
	
	@RequestMapping(value = "/toggleValid", method = RequestMethod.POST)
	@ResponseBody
	public void toggleValid(@RequestBody String commitId) throws Exception {
		handler.toggleValid(commitId);
	}
}
