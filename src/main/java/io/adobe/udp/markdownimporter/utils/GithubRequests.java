package io.adobe.udp.markdownimporter.utils;

import io.adobe.udp.markdownimporter.rest.RestClient;
import io.adobe.udp.markdownimporter.rest.RestClientResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class GithubRequests {
	
//	private static final Logger logger = LoggerFactory.getLogger(GithubRequests.class);

	public static String getDefaultBranch(String url, String token) {
		JSONObject json = (JSONObject) execute(url, token, null);
		if(json != null) {
			try {
				return json.getString(GithubConstants.DEFAULT_BRANCH);
			} catch (JSONException e) {
				System.out.println(e.getMessage() );
			}
		}
		return null;
	}
	
	public static String getFileUrl(String url, String token) throws MalformedURLException {
		JSONObject json = (JSONObject) execute(url, token, null);
		if(json != null) {
			try {
				return json.getString(GithubConstants.FILE_DOWNLOAD_URL);
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	} 
	
	
	public static Map<String, String> getShaMapping(String url, List<String> branches, String token ) {
		Map<String, String> branchSha = new HashMap<String, String>();
		try {
			JSONArray json = (JSONArray)   execute(url, token, null);
			for(int i = 0; i < json.length(); i++) {
				JSONObject branch = json.getJSONObject(i);
				String branchName = branch.getString(GithubConstants.NAME);
				if(branches.contains(branchName)) {
					JSONObject commit = branch.getJSONObject(GithubConstants.COMMIT);
					branchSha.put(branchName, commit.getString(GithubConstants.SHA));
				}
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return branchSha;
	}
	
	public static String getBranchSha(String url, String token) {
		JSONObject json = (JSONObject)  execute(url, token, null);
		if(json != null) {
			try {
				return ((JSONObject) json.get(GithubConstants.OBJECT)).get(GithubConstants.SHA).toString();
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	
	public static JSONObject executeDiffRequest(String url, String token) {
		return (JSONObject) execute(url, token, null);
	}
	
	public static JSONObject executeTreeRequest(String url, String token, boolean recursive) {
		if(recursive) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("recursive", "1");
			return (JSONObject)  execute(url, token, params);
		}
		return (JSONObject)  execute(url, token, null);
	}
	
	public static Date getCommitTimestamp(String url, String token) {
		JSONObject json = (JSONObject)  execute(url, token, null);
		try {
			JSONObject author = (JSONObject) ((JSONObject) json.get(GithubConstants.COMMIT)).get(GithubConstants.AUTHOR);
			String time = author.getString(GithubConstants.DATE).replaceAll("Z$", "+0000");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			return sdf.parse(time);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private static Object execute(String url, String token, Map<String, String> params) {
		RestClient restClient = params == null ? new RestClient(url) : new RestClient(url, params);
		if(StringUtils.isNotBlank(token)) {
			restClient.addHeader("Authorization", "token " + token);
		}
		RestClientResponse response;
		try {
			response = restClient.doGet();
			System.out.println("Connected to: " + url + "status: " + response.getStatus());
			if(response.getJson().trim().startsWith("{")) {
				return new JSONObject(response.getJson());
			} else {
				return new JSONArray(response.getJson());
			}
		} catch (IOException | JSONException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
