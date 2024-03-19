package ua.customer.mapper;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ua.customer.dto.request.CustomerRegisterRequest;

import java.util.Collections;
import java.util.List;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "username", source = "request.userName")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "credentials", source = "request", qualifiedByName = "mapCredentials")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "enabled", constant = "true")
    UserRepresentation toUserRepresentation(CustomerRegisterRequest request);
    @Named("mapCredentials")
    default List<CredentialRepresentation> mapCredentials(CustomerRegisterRequest request) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        return Collections.singletonList(credential);
    }
}
