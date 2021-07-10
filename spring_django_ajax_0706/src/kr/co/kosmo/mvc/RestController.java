package kr.co.kosmo.mvc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;


/**
 * @author 허태준 / 2021. 7. 6. / 오후 8:45:33
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	
	@RequestMapping(value = "/chartView")
	public String viewMessage() {
		return "Hello";
	}
	
	@GetMapping(value = "/json",produces="application/json")
	public JSONPObject jsonp(String callback,String useYymm,String sxClNm) throws Exception {
		//System.out.println(sxClNm);
		// 공공데이터 받아오기
	    String data = getData(useYymm, sxClNm); 

	    // json -> map
		ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    map = mapper.readValue(data, Map.class);
	    // 타고 타고 들어가기
	    Map<String, Object> res = (Map<String, Object>) map.get("response");
	    Map<String, Object> body = (Map<String, Object>) res.get("body");
	    List<Map<String,String>> items = (ArrayList<Map<String,String>>) body.get("items");
	    // System.out.println(items);
	    
	    // x 축 들어갈 카테고리 (Set 하나 List 하나.. 어쩌다 보니 이렇게함)
	    HashSet<String> x = new HashSet<String>(); 
	    List<String> list = new ArrayList<String>();
	    for (Map<String,String> item : items) {
	    	x.add(item.get("sndCtgyNm"));
	    	list.add(item.get("sndCtgyNm"));
	    }
	    // y 축에 들아갈 값
	    List<Integer> y = new ArrayList<Integer>(); 
	    for (String e : x) {
	    	// list 안에 카테고리 별 몇 개 씩 있는지 카운트
	    	int count = Collections.frequency(list, e);
	    	y.add(count);
	    }
	    
	    // 처리된 data를 맵으로 바꾸고 그걸 다시 JSONP로 바꾸고...어쩌다보니2
	    Map<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("columns", x);
	    map2.put("data", y);
	    System.out.println(map2);
	    // 첫번째 인자로 콜백, 두번째엔 넘길 데이터
	    JSONPObject result = new JSONPObject(callback, map2);
	    System.out.println("getF: "+result.getFunction());
	    System.out.println("getV: "+result.getValue());
	    return result;
		
	}
	
	
	private String getData(String useYymm,String sxClNm) throws Exception {
		// url 주소 쌓기
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552640/GwpCardUseDtlInfoService/selectListUseDtl?serviceKey=");
		String api_key = "btFSHA8ipx4SG4RD2p%2B8sRRPbpCzgc5nMmWI%2BUmbHsgy0qeT8sZIglEdjCoUKh%2BPDMLQxkPkqaLyVeH55gWrPg%3D%3D";
		// 요청 파라미터들 추가
		urlBuilder.append(api_key );
		urlBuilder.append("&viewType=json&numOfRows=100&pageNo=1&useYymm=");
		urlBuilder.append(useYymm);
		urlBuilder.append("&sxClNm=");
		urlBuilder.append(sxClNm);
		urlBuilder.append("&fstCtgy=A");
		
		// URL 연결
		URL url = new URL(urlBuilder.toString());
		//System.out.println(url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode()); // 200
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
        	// 한글이 깨져서 "UTF-8" 설정
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString()); //{"response":{"body":{"items":[{"crdCoBzkNm":"기타휴게음식점","
		return sb.toString();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/json2", 
			produces = "application/json; charset=euc-kr") /// 인코딩 추가했더니 한글 안깨져
	public String chart(String callback) {
		/*
		 * {"columns": ["student", "kor", "eng", "math"], 
		 * "index": [0, 1, 2, 3, 4, 5], 
		 * "data": [["홍길동", 85, 90, 85], 
		 * ["이순자", 95, 95, 75], 
		 * ["왕서방", 85, 95, 75], 
		 * ["영심이", 80, 80, 100], 
		 * ["호철이", 90, 65, 70], 
		 * ["가진이", 75, 100, 80]]}
		 * */
		Map<String, Object> map = new HashMap<>();
		List<String> columns = new ArrayList<String>();
		columns.add("student");
		columns.add("kor");
		columns.add("eng");
		columns.add("math");
		List<Integer> index = new ArrayList<Integer>();
		index.add(0);
		index.add(1);
		index.add(2);
		index.add(3);
		index.add(4);
		index.add(5);
		List<List<Object>> data = new ArrayList<List<Object>>();
		List<Object> data1 = new ArrayList<Object>();
		data1.add("홍길동");
		data1.add(85);
		data1.add(90);
		data1.add(85);
		List<Object> data2 = new ArrayList<Object>();
		data2.add("영심이");
		data2.add(80);
		data2.add(80);
		data2.add(100);
		data.add(data1);
		data.add(data2);
		map.put("columns", columns);
		map.put("index", index);
		map.put("data", data);
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
        
		try {
			result = mapper.writeValueAsString(map);
			System.out.println("---------result---------");
			System.out.println(result);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		return result;
	}

}
