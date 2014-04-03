package org.gitistics.controllers;

import javax.inject.Inject;

import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticFilter;
import org.gitistics.statistic.StatisticsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/statistics")
public class StatisticsController {
	
	@Inject
	private StatisticsProvider provider;

	@RequestMapping(value = "/byYear", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Statistic> byYear(StatisticFilter filter) throws Exception {
		return provider.byYear(filter);
	}
}
