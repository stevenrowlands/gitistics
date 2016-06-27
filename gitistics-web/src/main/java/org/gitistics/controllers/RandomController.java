package org.gitistics.controllers;

import javax.inject.Inject;

import org.gitistics.statistic.RandomStatisticsProvider;
import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/random")
public class RandomController {
	
	@Inject
	private RandomStatisticsProvider provider;

	@RequestMapping(value = "/revertself", method = RequestMethod.POST)
	@ResponseBody
	public Iterable<Statistic> revertself(@RequestBody StatisticParam filter) throws Exception {
		return provider.revertself(filter.getRepositoryName());
	}
	
	@RequestMapping(value = "/streaker", method = RequestMethod.POST)
	@ResponseBody
	public Iterable<Statistic> streaker(@RequestBody StatisticParam filter) throws Exception {
		return provider.streaker(filter.getRepositoryName());
	}
}
