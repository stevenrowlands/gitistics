package org.gitistics.controllers;

import org.eclipse.jgit.storage.file.FileRepository;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.RepoRepository;
import org.gitistics.repository.AsynchronousRepositoryWrapper;
import org.gitistics.repository.RepositoryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/repository")
public class RepositoryController {
 
	@Autowired
	private RepoRepository repositories;
	
	@Autowired
	private RepositoryProcessor repositoryProcessor;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Repo> getRepositories() {
		return repositories.findAll();
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
    @ResponseBody
    public Repo addRepository(@RequestBody Repo repo) throws Exception {
		if (repo.getLocation() != null) {
			repo.setLocation(new FileRepository(repo.getLocation()).getDirectory().getAbsolutePath());
			repositories.save(repo);
			new AsynchronousRepositoryWrapper(repositoryProcessor, repo).process(repo);
		}
        return repo;
    }
	
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
    @ResponseBody
    public Repo deleteRepository(@RequestParam("location") String location) throws Exception {
		Repo repo = repositories.findOne(location);
		if (repo != null) {
			repositories.delete(repo); 
		}
        return repo;
    }
	
}