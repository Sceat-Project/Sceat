package hu.sceat.backend.presentation.shell;

import hu.sceat.backend.business.service.OrganizationService;
import hu.sceat.backend.presentation.util.TextResponseUtil;
import org.springframework.shell.command.annotation.Command;

@Command(command="organization")
public class OrganizationCommands {
	
	private final OrganizationService orgService;
	
	public OrganizationCommands(OrganizationService orgService) {
		this.orgService = orgService;
	}
	
	@Command(command = "list")
	public String list() {
		return TextResponseUtil.respondOk(orgService.list());
	}
	
	@Command(command = "create")
	public String create(String name) {
		return orgService.create(name)
				.get(TextResponseUtil::respondOk, TextResponseUtil::respondFail);
	}
	
	@Command(command = "delete")
	public String delete(Long id) {
		return orgService.delete(id)
				.get(TextResponseUtil::respondOk, TextResponseUtil::respondFail);
	}
}
