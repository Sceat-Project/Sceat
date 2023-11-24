package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.OrganizationDto;
import hu.sceat.backend.business.dto.UserRefDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Organization;
import hu.sceat.backend.persistence.entity.Server;
import hu.sceat.backend.persistence.repository.OrganizationRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrganizationService {
	
	private final OrganizationRepository orgRepo;
	
	public OrganizationService(OrganizationRepository orgRepo) {
		this.orgRepo = orgRepo;
	}
	
	@Transactional
	public Try<OrganizationDto, Fail> findById(Long organizationId) {
		return get(organizationId)
				.map(DtoMapper.INSTANCE::toOrganization);
	}
	
	@Transactional
	public Try<Collection<UserRefDto>, Fail> listServers(UserId requester, Long organizationId) {
		return resolveOrgWhereServer(requester, organizationId)
				.map(o -> o.getServers().stream()
						.map(Server::getUser)
						.map(DtoMapper.INSTANCE::toUserRef)
						.toList());
	}
	
	@Transactional
	public Try<Collection<UserRefDto>, Fail> listConsumers(UserId requester, Long organizationId) {
		return resolveOrgWhereServer(requester, organizationId)
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
				.map(Organization::create)
				.map(orgRepo::save)
				.map(DtoMapper.INSTANCE::toOrganization);
	}
	
	@Transactional
	public Try<Organization, Fail> resolveOrgWhereServer(UserId requester, Long organizationId) {
		return get(organizationId)
				.filter(org -> org.getServers().stream()
								.anyMatch(server -> server.getUser().getId().equals(requester.getId())),
						CommonFail.forbidden("organization " + organizationId));
	}
	
	@Transactional
	public Try<Organization, Fail> resolveOrgWhereConsumer(UserId requester, Long organizationId) {
		return get(organizationId)
				.filter(org -> org.getConsumers().stream()
								.anyMatch(consumer -> consumer.getUser().getId().equals(requester.getId())),
						CommonFail.forbidden("organization " + organizationId));
	}
	
	@Transactional
	public Try<Organization, Fail> get(Long organizationId) {
		return Try.from(orgRepo.findById(organizationId),
				CommonFail.notFound("organization " + organizationId));
	}
}
