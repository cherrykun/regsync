package com.regsync.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
@EnableScheduling
public class ParseJsonService {
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		// HttpClientのインスタンスを作る（HTTPリクエストを送るために必要）
		HttpClient client = new DefaultHttpClient();
		ArrayList<Object> list = new ArrayList<Object>(); 
		
		for(int i=1; i<=10; i++) {
		// HttpGetのインスタンスを作る（GETリクエストを送るために必要）
		HttpGet httpGet = new HttpGet("http://fg-69c8cbcd.herokuapp.com/user/" + i);
		try {
			// リクエストしたリンクが存在するか確認するために、HTTPリクエストを送ってHTTPレスポンスを取得する
			HttpResponse response = client.execute(httpGet);
			// 返却されたHTTPレスポンスの中のステータスコードを調べる
			// -> statusCodeが200だったらページが存在。404だったらNot found（ページが存在しない）。500はInternal server
			// error。
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// HTTPレスポンスが200よりページは存在する
				// レスポンスからHTTPエンティティ（実体）を生成
				HttpEntity entity = response.getEntity();
				// HTTPエンティティからコンテント（中身）を生成
				InputStream content = entity.getContent();
				// コンテントからInputStreamReaderを生成し、さらにBufferedReaderを作る
				// InputStreamReaderはテキストファイル（InputStream）を読み込む
				// BufferedReaderはテキストファイルを一行ずつ読み込む
				// （参考）http://www.tohoho-web.com/java/file.htm
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				// readerからreadline()で行を読んで、builder文字列(StringBuilderクラス)に格納していく。
				// ※このプログラムの場合、lineは一行でなのでループは回っていない
				// ※BufferedReaderを使うときは一般にこのように記述する。
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					System.out.println("builder.append(line):" + builder.append(line));
					System.out.println("line:" + line);
					//JSONからJavaオブジェクトに変換
					Object obj = getObjFromJSON(line);
					System.out.println(obj);
					//リストに格納
					list.add(obj);
				}
			} else {
				System.out.println("Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		System.out.println(list);
	}

	//JSONをJavaオブジェクトに変換
	private static Object getObjFromJSON(String line) {
		ObjectMapper mapper = new ObjectMapper(); 
		Object obj = null;
	        try {
	            obj = mapper.readValue(line, Object.class);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return obj;
	}
}
