package com.regnosys.granite.projector.isdacreate;

import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.isda.isdacreate.isda.csaim2016.jpnlaw.IsdaCreateIsdaCsaIm2016JpnLaw;
import org.isda.isdacreate.isda.csaim2016.nylaw.IsdaCreateIsdaCsaIm2016NyLaw;
import org.isda.isdacreate.isda.csdim2016.englaw.IsdaCreateIsdaCsdIm2016EngLaw;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class IsdaCreateTest {

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsdIm2016EngLaw1() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csd-eng-law/2.3/sample1.json", IsdaCreateIsdaCsdIm2016EngLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsdIm2016EngLaw2() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csd-eng-law/2.3/sample2.json", IsdaCreateIsdaCsdIm2016EngLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsdIm2016EngLaw3() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csd-eng-law/2.3/sample3.json", IsdaCreateIsdaCsdIm2016EngLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateInitialMargin2016JpnLaw501() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csa-jpn-law/1.4/sample1.json", IsdaCreateIsdaCsaIm2016JpnLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsaIm2016JpnLaw1() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csa-jpn-law/1.4/sample2.json", IsdaCreateIsdaCsaIm2016JpnLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsaIm2016JpnLaw2() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/other/sandbox/2016-im-csa-jpn-law/1.4/sample3.json", IsdaCreateIsdaCsaIm2016JpnLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsaIm2016NyLaw1() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/test-pack/production/2016-im-csa-ny-law/0.5/tp1-metadata1.json", IsdaCreateIsdaCsaIm2016NyLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	@Test
	void shouldDeserialiseIsdaCreateIsdaCsaIm2016NyLaw2() throws IOException {
		var isdaCreate = deserialize("cdm-sample-files/isda-create/test-pack/production/2016-im-csa-ny-law/0.5/tp1-metadata3.json", IsdaCreateIsdaCsaIm2016NyLaw.class);
		assertNotNull(isdaCreate);
		// TODO add asserts
	}

	private <T> T deserialize(String path, Class<T> clazz) throws IOException {
		URL resource = Resources.getResource(path);
		String json = removeBom(Resources.toString(resource, Charset.defaultCharset()));

		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}

	private static final char BOM = '\uFEFF';

	private static String removeBom(String s) {
		char[] charArray = s.toCharArray();
		if (charArray != null && charArray.length > 0 && charArray[0] == BOM)
			return new String(Arrays.copyOfRange(charArray, 1, charArray.length));
		return s;
	}
}
