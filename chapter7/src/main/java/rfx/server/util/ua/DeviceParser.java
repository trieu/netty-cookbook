/**
 * Copyright 2012 Twitter, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rfx.server.util.ua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Device parser using ua-parser regexes. Extracts device information from user
 * agent strings.
 * 
 * @author Steve Jiang (@sjiang) <gh at iamsteve com>
 */
public class DeviceParser {
	public static final Map<String, String> mobileDeviceTypeMap = new HashMap<String, String>();
	static {
		mobileDeviceTypeMap.put("iOS", "General_Mobile");
		mobileDeviceTypeMap.put("Android", "General_Mobile");
		mobileDeviceTypeMap.put("Windows Phone", "General_Mobile");
		mobileDeviceTypeMap.put("BlackBerry OS", "General_Mobile");
	}
	
	List<DevicePattern> patterns;

	public DeviceParser(List<DevicePattern> patterns) {
		this.patterns = patterns;
	}
	public List<DevicePattern> getPatterns() {
		return patterns;
	}
	public Device parse(String agentString, OS os) {
		String device = null;
		for (DevicePattern p : patterns) {
			if ((device = p.match(agentString)) != null) {
				break;
			}
		}
		if (device == null){			
			device = "Other";
		}

		Device dv = new Device(device);
		String deviceType = mobileDeviceTypeMap.get(os.family);
		if(deviceType != null){
			if(deviceType.equals("General_Mobile")){
				if( ! agentString.toLowerCase().contains("mobile") ){
					deviceType = "General_Tablet";
				}
			}
			dv.setDeviceType(deviceType);	
		}	
		return dv;
	}

	public static DeviceParser fromList(List<Map> configList) {
		List<DevicePattern> configPatterns = new ArrayList<DevicePattern>();
		for (Map<String, String> configMap : configList) {
			configPatterns.add(DeviceParser.patternFromMap(configMap));
		}
		return new DeviceParser(configPatterns);
	}

	protected static DevicePattern patternFromMap(Map<String, String> configMap) {
		String regex = configMap.get("regex");
		if (regex == null) {
			throw new IllegalArgumentException("Device is missing regex");
		}
		return new DevicePattern(Pattern.compile(regex),
				configMap.get("device_replacement"));
	}

	protected static class DevicePattern {
		private final Pattern pattern;
		private final String familyReplacement;

		public DevicePattern(Pattern pattern, String familyReplacement) {
			this.pattern = pattern;
			this.familyReplacement = familyReplacement;
		}
		
		public String getFamilyReplacement() {
			return familyReplacement;
		}

		public String match(String agentString) {
			Matcher matcher = pattern.matcher(agentString);

			if (!matcher.find()) {
				return null;
			}

			String family = null;
			if (familyReplacement != null) {
				if (familyReplacement.contains("$1")
						&& matcher.groupCount() >= 1
						&& matcher.group(1) != null) {
					family = familyReplacement.replaceFirst("\\$1",
							Matcher.quoteReplacement(matcher.group(1)));
				} else {
					family = familyReplacement;
				}
			} else if (matcher.groupCount() >= 1) {
				family = matcher.group(1);
			}
			return family;
		}
	}
 
	
	
	
	public static String parseDeviceType(String ua){
		boolean isIPhone = ua.toLowerCase().contains("mobile");
		boolean isAndroid = ua.toLowerCase().contains("android");
		boolean isWindowPhone = ua.toLowerCase().contains("phone");
		return "";
	}
}