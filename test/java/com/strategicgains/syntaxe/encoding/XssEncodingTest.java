/*
    Copyright 2014, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.strategicgains.syntaxe.encoding;

import static org.junit.Assert.*;

import org.junit.Test;

import com.strategicgains.syntaxe.ValidationEngine;
import com.strategicgains.syntaxe.annotation.encoding.EncodingProvider;
import com.strategicgains.syntaxe.annotation.encoding.HtmlEncoded;
import com.strategicgains.syntaxe.annotation.encoding.JavascriptEncoded;
import com.strategicgains.syntaxe.annotation.encoding.WebEncoded;
import com.strategicgains.syntaxe.annotation.encoding.XmlEncoded;

/**
 * @author toddf
 * @since Mar 5, 2014
 */
public class XssEncodingTest
{
	@Test
	public void shouldEncodeJavascript()
	{
		Inner object = new Inner();
		object.setJavascript("'; alert(1); //");
		ValidationEngine.validate(object);
		// System.out.println(object.getJavascript());
		assertEquals("\\x27; alert(1); \\/\\/", object.getJavascript());
	}

	@Test
	public void shouldEncodeXml()
	{
		Inner object = new Inner();
		object.setXml("\"><script>alert('XSS')</script>");
		ValidationEngine.validate(object);
		// System.out.println(object.getXml());
		assertEquals("&#34;&gt;&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;", object.getXml());
	}

	@Test
	public void shouldEncodeHtml()
	{
		Inner object = new Inner();
		object.setHtml("\"><script>alert('XSS')</script>");
		ValidationEngine.validate(object);
		// System.out.println(object.getHtml());
		assertEquals("&#34;&gt;&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;", object.getHtml());
	}

	@Test
	public void shouldEncodeAll()
	{
		Inner object = new Inner();
		String unknown = "';alert(String.fromCharCode(88,83,83))//\';alert(String.fromCharCode(88,83,83))//\";alert(String.fromCharCode(88,83,83))//\\\";alert(String.fromCharCode(88,83,83))//--></SCRIPT>\">'><SCRIPT>alert(String.fromCharCode(88,83,83))</SCRIPT>";
		object.setJavascript(unknown);
		object.setHtml(unknown);
		object.setAll(unknown);
		ValidationEngine.validate(object);
		// System.out.println(object.getAll());
		assertEquals("\\x27;alert(String.fromCharCode(88,83,83))\\/\\/\\x27;alert(String.fromCharCode(88,83,83))\\/\\/\\x22;alert(String.fromCharCode(88,83,83))\\/\\/\\\\\\x22;alert(String.fromCharCode(88,83,83))\\/\\/\\-\\-&gt;&lt;\\/SCRIPT&gt;\\x22&gt;\\x27&gt;&lt;SCRIPT&gt;alert(String.fromCharCode(88,83,83))&lt;\\/SCRIPT&gt;", object.getAll());
		assertNotEquals(object.getJavascript(), object.getAll());
		assertNotEquals(object.getHtml(), object.getAll());
	}

	@Test
	public void shouldEncodeWeb()
	{
		Inner object = new Inner();
		String unknown = "';alert(String.fromCharCode(88,83,83))//\';alert(String.fromCharCode(88,83,83))//\";alert(String.fromCharCode(88,83,83))//\\\";alert(String.fromCharCode(88,83,83))//--></SCRIPT>\">'><SCRIPT>alert(String.fromCharCode(88,83,83))</SCRIPT>";
		object.setWeb(unknown);
		object.setAll(unknown);
		ValidationEngine.validate(object);
		// System.out.println(object.getWeb());
		assertEquals("\\x27;alert(String.fromCharCode(88,83,83))\\/\\/\\x27;alert(String.fromCharCode(88,83,83))\\/\\/\\x22;alert(String.fromCharCode(88,83,83))\\/\\/\\\\\\x22;alert(String.fromCharCode(88,83,83))\\/\\/\\-\\-&gt;&lt;\\/SCRIPT&gt;\\x22&gt;\\x27&gt;&lt;SCRIPT&gt;alert(String.fromCharCode(88,83,83))&lt;\\/SCRIPT&gt;", object.getWeb());
		assertEquals(object.getAll(), object.getWeb());
	}

	@Test
	public void shouldUseMyEncoder()
	{
		Inner object = new Inner();
		object.setMyString("somethinggoeshere");
		ValidationEngine.validate(object);
//		System.out.println(object.getMyString());
		assertEquals("encoded=somethinggoeshere", object.getMyString());
	}

	private class Inner
	{
		@JavascriptEncoded
		private String javascript;

		@XmlEncoded
		private String xml;

		@HtmlEncoded
		private String html;

		@JavascriptEncoded
		@HtmlEncoded
		private String all;

		@WebEncoded
		private String web;
		
		@EncodingProvider(MyEncoder.class)
		private String myString;

		public String getJavascript()
		{
			return javascript;
		}

		public void setJavascript(String javascript)
		{
			this.javascript = javascript;
		}

		public String getXml()
		{
			return xml;
		}

		public void setXml(String xml)
		{
			this.xml = xml;
		}

		public String getHtml()
		{
			return html;
		}

		public void setHtml(String html)
		{
			this.html = html;
		}

		public String getAll()
		{
			return all;
		}

		public void setAll(String all)
		{
			this.all = all;
		}

		public String getWeb()
		{
			return web;
		}

		public void setWeb(String web)
		{
			this.web = web;
		}

		public String getMyString()
		{
			return myString;
		}

		public void setMyString(String myString)
		{
			this.myString = myString;
		}
	}
}
