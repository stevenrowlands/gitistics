package org.gitistics.jpa.statistics;

import java.sql.Date;
import java.util.UUID;

import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.CommitFile;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.CommitFileRepository;
import org.gitistics.jpa.repository.CommitRepository;
import org.gitistics.jpa.repository.RepoRepository;
import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataGenerator {

	@Autowired 
	private CommitFileRepository commitFileRepository;
	
	@Autowired
	private CommitRepository commitRepository;
	
	@Autowired
	private RepoRepository repoRepository;
	
	public void generate(String author, DateTime current, ReadablePeriod increment, int repeat, int lines) {
		for (int i = 0; i < repeat; i++) {
			Repo r = new Repo("/some/dir/.git");
			r.setName("repo1");
			repoRepository.save(r);
			
			Commit c = new Commit(r, UUID.randomUUID().toString().replaceAll("-", ""));
			c.setCommitDate(new Date(current.getMillis()));
			c.setRepo(r);
			c.setAuthorName(author);
			c.setLinesAdded(lines);
			c.setLinesRemoved(lines);
			commitRepository.save(c);
			
			CommitFile cf = new CommitFile(c);
			cf.setCommit(c);
			cf.setFileName("ABC");
			cf.setLinesAdded(lines);
			cf.setLinesRemoved(0);
			commitFileRepository.save(cf);
			
			cf = new CommitFile(c);
			cf.setCommit(c);
			cf.setFileName("DEF");
			cf.setLinesAdded(0);
			cf.setLinesRemoved(lines);
			commitFileRepository.save(cf);
			
			current = current.plus(increment);
		}
	}
}
