/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample;

import com.ibm.mfp.adapter.api.OAuthSecurity;
import com.ibm.mfp.server.registration.external.model.ClientData;
import com.ibm.mfp.server.registration.external.model.PersistentAttributes;
import com.ibm.mfp.server.security.external.resource.AdapterSecurityContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
public class EnrollmentResource {

	@Context
	AdapterSecurityContext adapterSecurityContext;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/publicData")
	public String getPublicData(){
		return "Lorem ipsum dolor sit amet, modo oratio cu nam, mei graece dicunt tamquam ne.";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@OAuthSecurity(scope = "accessRestricted")
	@Path("/balance")
	public String getBalance(){
		return "19938.80";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@OAuthSecurity(scope = "transactionsPrivilege")
	@Path("/transactions")
	public String getTransactions(){
		return "{'date':'12/01/2016', 'amount':'19938.80'}";
	}

	@GET
	@Path("/isEnrolled")
	public boolean isEnrolled(){
		PersistentAttributes protectedAttributes = adapterSecurityContext.getClientRegistrationData().getProtectedAttributes();
		return (protectedAttributes.get("pinCode") != null);
	}

	@POST
	@OAuthSecurity(scope = "setPinCode")
	@Path("/setPinCode/{pinCode}")
	public Response setPinCode(@PathParam("pinCode") String pinCode){
		ClientData clientData = adapterSecurityContext.getClientRegistrationData();
		clientData.getProtectedAttributes().put("pinCode", pinCode);
		adapterSecurityContext.storeClientRegistrationData(clientData);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deletePinCode")
	public Response deletePinCode(){
		ClientData clientData = adapterSecurityContext.getClientRegistrationData();
		if (clientData.getProtectedAttributes().get("pinCode") != null){
			clientData.getProtectedAttributes().delete("pinCode");
			adapterSecurityContext.storeClientRegistrationData(clientData);
		}
		return Response.ok().build();
	}





}
