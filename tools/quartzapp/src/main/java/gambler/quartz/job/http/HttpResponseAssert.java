package gambler.quartz.job.http;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

public class HttpResponseAssert {

	private String type;// text or json

	private String actual;

	/**
	 * text assert required field
	 */
	private String expect;

	/**
	 * json assert required field
	 */
	private String[] fields;

	/**
	 * json assert required field
	 */
	private String[] expects;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getExpects() {
		return expects;
	}

	public void setExpects(String[] expects) {
		this.expects = expects;
	}

	/**
	 * <ul>
	 * <li>false : assert fail</li>
	 * <li>true : assert success</li>
	 * </ul>
	 */
	public boolean assertResponse() {
		if ("text".equalsIgnoreCase(type)) {
			if (actual == null || expect == null) {
				return false;
			}
			return expect.equals(actual);
		} else if ("json".equalsIgnoreCase(type)) {
			if (fields.length != expects.length) {
				throw new UnsupportedOperationException("length must be same");
			}
			if (actual == null) {
				return false;
			}
			JSONObject result = JSONObject.parseObject(actual);
			for (int i = 0; i < fields.length; i++) {
				String actualField = result.getString(fields[i]);
				if (actualField == null) {
					return false;
				}
				boolean success = actualField.equals(expects[i]);
				if (!success) {
					return false;
				}
			}
			return true;
		} else {
			throw new UnsupportedOperationException("unsupported assert type");
		}
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	@Override
	public String toString() {
		return "HttpResponseAssert [type=" + type + ", actual=" + actual + ", expect=" + expect + ", fields="
				+ Arrays.toString(fields) + ", expects=" + Arrays.toString(expects) + "]";
	}
}
