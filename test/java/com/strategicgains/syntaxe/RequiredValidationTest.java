package com.strategicgains.syntaxe;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.Required;

public class RequiredValidationTest
{
	@Test
	public void shouldFailOnNullUuid()
	{
		InnerDomain o = new InnerDomain();
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).startsWith("UUID"));
		assertTrue(errors.get(0).endsWith("required"));
	}

	@Test
	public void shouldPassOnUuid()
	{
		InnerDomain o = new InnerDomain();
		o.uuid = UUID.randomUUID();
		List<String> errors = ValidationEngine.validate(o);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldPassOnUuidArray()
	{
		InnerDomainWithArray o = new InnerDomainWithArray();
		o.uuids = new UUID[] {UUID.randomUUID(), UUID.randomUUID()};
		List<String> errors = ValidationEngine.validate(o);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailOnNullUuidArray()
	{
		InnerDomainWithArray o = new InnerDomainWithArray();
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).startsWith("UUID"));
		assertTrue(errors.get(0).endsWith("required"));
	}

	@Test
	public void shouldFailOnNullUuidArrayElement()
	{
		InnerDomainWithArray o = new InnerDomainWithArray();
		o.uuids = new UUID[] {UUID.randomUUID(), null, UUID.randomUUID(), null};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).startsWith("UUID[1]"));
		assertTrue(errors.get(0).endsWith("required"));
		assertTrue(errors.get(1).startsWith("UUID[3]"));
		assertTrue(errors.get(1).endsWith("required"));
	}

	@Test
	public void shouldFailOnEmptyUuidArray()
	{
		InnerDomainWithArray o = new InnerDomainWithArray();
		o.uuids = new UUID[] {};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).startsWith("UUID"));
		assertTrue(errors.get(0).endsWith("required"));
	}

	
	private class InnerDomain
	{
		@Required("UUID")
		public UUID uuid;
	}
	
	private class InnerDomainWithArray
	{
		@Required("UUID")
		public UUID[] uuids;
	}
}
