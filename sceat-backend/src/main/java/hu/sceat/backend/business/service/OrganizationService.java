package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.OrganizationDto;
import hu.sceat.backend.business.dto.OrganizationRefDto;
import hu.sceat.backend.business.dto.UserRefDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Organization;
import hu.sceat.backend.persistence.entity.Server;
import hu.sceat.backend.persistence.repository.OrganizationRepository;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import hu.sceat.backend.util.Unit;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

@Service
public class OrganizationService {
	
	private final OrganizationRepository orgRepo;
	private final UserRepository userRepo;
	
	public OrganizationService(OrganizationRepository orgRepo, UserRepository userRepo) {
		this.orgRepo = orgRepo;
		this.userRepo = userRepo;
	}
	
	@Transactional
	public Try<OrganizationDto, Fail> findById(Long organizationId) {
		return get(organizationId)
				.map(DtoMapper.INSTANCE::toOrganization);
	}
	
	@Transactional
	public Collection<OrganizationRefDto> list() {
		return orgRepo.findAll().stream()
				.map(DtoMapper.INSTANCE::toOrganizationRef)
				.toList();
	}
	
	@Transactional
	public Try<Collection<MenuDto>, Fail> listMenus(UserId requester, Long organizationId,
			LocalDate startDate, LocalDate endDate) {
		//TODO optimize this by querying menus directly (adding the date filter to the query)
		return getWhereMember(requester, organizationId)
				.map(o -> o.getMenus().stream()
						.filter(menu -> (menu.getDate().isEqual(startDate) || menu.getDate().isAfter(startDate))
								&& menu.getDate().isBefore(endDate))
						.map(DtoMapper.INSTANCE::toMenu)
						.toList());
	}
	
	@Transactional
	public Try<Collection<UserRefDto>, Fail> listServers(UserId requester, Long organizationId) {
		return getWhereServer(requester, organizationId)
				.map(o -> o.getServers().stream()
						.map(Server::getUser)
						.map(DtoMapper.INSTANCE::toUserRef)
						.toList());
	}
	
	@Transactional
	public Try<Collection<UserRefDto>, Fail> listConsumers(UserId requester, Long organizationId) {
		return getWhereServer(requester, organizationId)
				.map(o -> o.getConsumers().stream()
						.map(Consumer::getUser)
						.map(DtoMapper.INSTANCE::toUserRef)
						.toList());
	}
	
	@Transactional
	public Try<OrganizationDto, Fail> create(String name) {
		return Try.<String, Fail>success(name)
				.filter(n -> n.matches(Validation.GENERAL_NAME_REGEX),
						CommonFail.invalidInputFormat("name"))
				.filter(n -> orgRepo.findByName(n).isEmpty(),
						CommonFail.invalidInputAlreadyTaken("name"))
				.map(Organization::create)
				.map(orgRepo::save)
				.map(DtoMapper.INSTANCE::toOrganization);
	}
	
	@Transactional
	public Try<Unit, Fail> delete(Long id) {
		return get(id)
				//TODO are any extra checks needed?
				.map(org -> {
					org.getServers().forEach(server -> userRepo.delete(server.getUser()));
					org.getConsumers().forEach(consumer -> userRepo.delete(consumer.getUser()));
					orgRepo.delete(org);
					return Unit.get();
				});
	}
	
	@Transactional
	Try<Organization, Fail> getWhereServer(UserId requester, Long organizationId) {
		return getWhereMember(requester, organizationId)
				.filter(org -> org.getServers().stream()
								.anyMatch(server -> server.getUser().getId().equals(requester.getId())),
						CommonFail.forbidden("not a server"));
	}
	
	@Transactional
	Try<Organization, Fail> getWhereMember(UserId requester, Long organizationId) {
		//TODO this could be optimized
		return get(organizationId)
				.filter(org -> Stream.concat(
								org.getServers().stream().map(Server::getUser),
								org.getConsumers().stream().map(Consumer::getUser)
						).anyMatch(user -> user.getId().equals(requester.getId())),
						CommonFail.notFound("organization " + organizationId));
	}
	
	@Transactional
	Try<Organization, Fail> get(Long organizationId) {
		return Try.from(orgRepo.findById(organizationId),
				CommonFail.notFound("organization " + organizationId));
	}
}
