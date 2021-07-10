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
 * @author ������ / 2021. 7. 6. / ���� 8:45:33
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
		// ���������� �޾ƿ���
	    String data = getData(useYymm, sxClNm); 

	    // json -> map
		ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    map = mapper.readValue(data, Map.class);
	    // Ÿ�� Ÿ�� ����
	    Map<String, Object> res = (Map<String, Object>) map.get("response");
	    Map<String, Object> body = (Map<String, Object>) res.get("body");
	    List<Map<String,String>> items = (ArrayList<Map<String,String>>) body.get("items");
	    // System.out.println(items);
	    
	    // x �� �� ī�װ� (Set �ϳ� List �ϳ�.. ��¼�� ���� �̷�����)
	    HashSet<String> x = new HashSet<String>(); 
	    List<String> list = new ArrayList<String>();
	    for (Map<String,String> item : items) {
	    	x.add(item.get("sndCtgyNm"));
	    	list.add(item.get("sndCtgyNm"));
	    }
	    // y �࿡ ��ư� ��
	    List<Integer> y = new ArrayList<Integer>(); 
	    for (String e : x) {
	    	// list �ȿ� ī�װ� �� �� �� �� �ִ��� ī��Ʈ
	    	int count = Collections.frequency(list, e);
	    	y.add(count);
	    }
	    
	    // ó���� data�� ������ �ٲٰ� �װ� �ٽ� JSONP�� �ٲٰ�...��¼�ٺ���2
	    Map<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("columns", x);
	    map2.put("data", y);
	    System.out.println(map2);
	    // ù��° ���ڷ� �ݹ�, �ι�°�� �ѱ� ������
	    JSONPObject result = new JSONPObject(callback, map2);
	    System.out.println("getF: "+result.getFunction());
	    System.out.println("getV: "+result.getValue());
	    return result;
		
	}
	
	
	private String getData(String useYymm,String sxClNm) throws Exception {
		// url �ּ� �ױ�
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552640/GwpCardUseDtlInfoService/selectListUseDtl?serviceKey=");
		String api_key = "btFSHA8ipx4SG4RD2p%2B8sRRPbpCzgc5nMmWI%2BUmbHsgy0qeT8sZIglEdjCoUKh%2BPDMLQxkPkqaLyVeH55gWrPg%3D%3D";
		// ��û �Ķ���͵� �߰�
		urlBuilder.append(api_key );
		urlBuilder.append("&viewType=json&numOfRows=100&pageNo=1&useYymm=");
		urlBuilder.append(useYymm);
		urlBuilder.append("&sxClNm=");
		urlBuilder.append(sxClNm);
		urlBuilder.append("&fstCtgy=A");
		
		// URL ����
		URL url = new URL(urlBuilder.toString());
		//System.out.println(url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode()); // 200
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
        	// �ѱ��� ������ "UTF-8" ����
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
        System.out.println(sb.toString()); //{"response":{"body":{"items":[{"crdCoBzkNm":"��Ÿ�ް�������","
		return sb.toString();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/json2", 
			produces = "application/json; charset=euc-kr") /// ���ڵ� �߰��ߴ��� �ѱ� �ȱ���
	public String chart(String callback) {
		/*
		 * {"columns": ["student", "kor", "eng", "math"], 
		 * "index": [0, 1, 2, 3, 4, 5], 
		 * "data": [["ȫ�浿", 85, 90, 85], 
		 * ["�̼���", 95, 95, 75], 
		 * ["�ռ���", 85, 95, 75], 
		 * ["������", 80, 80, 100], 
		 * ["ȣö��", 90, 65, 70], 
		 * ["������", 75, 100, 80]]}
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
		data1.add("ȫ�浿");
		data1.add(85);
		data1.add(90);
		data1.add(85);
		List<Object> data2 = new ArrayList<Object>();
		data2.add("������");
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
