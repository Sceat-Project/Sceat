package hu.sceat.backend.presentation.shell;

import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.business.service.ServerService;
import hu.sceat.backend.presentation.util.TextResponseUtil;
import org.springframework.shell.command.annotation.Command;

@Command(command="server")
public class ServerCommands {
	
	private final AuthService authService;
	private final ServerService serverService;
	
	public ServerCommands(AuthService authService, ServerService serverService) {
		this.authService = authService;
		this.serverService = serverService;
	}
	
	@Command(command = "register")
	public String register(String email, String password, String name, Long organizationId) {
		return authService.registerServer(email, password, name, organizationId)
				.get(TextResponseUtil::respondOk, TextResponseUtil::respondFail);
	}
}
