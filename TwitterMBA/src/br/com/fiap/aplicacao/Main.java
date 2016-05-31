package br.com.fiap.aplicacao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.fiap.ordenadores.OrdenadorStatusData;
import br.com.fiap.ordenadores.OrdenadorStatusNome;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) throws TwitterException {
		
		final String tagUtilizada = "#java8";
		
		//final String twitterProfessor = "@mirandandreza";
		final String twitterProfessor = "@michelpf";
		
		final String authConsumerKey = "";
		final String authConsumerSecret = "";
		final String authAccessToken = "";
		final String authAccessTokenSecret = "";
	
		final ConfigurationBuilder cb = new ConfigurationBuilder ();

		cb.setOAuthConsumerKey(authConsumerKey);
		cb.setOAuthConsumerSecret(authConsumerSecret);
		cb.setOAuthAccessToken(authAccessToken);
		cb.setOAuthAccessTokenSecret(authAccessTokenSecret);
		
		final TwitterFactory tf = new TwitterFactory (cb.build ());
		
		final Twitter twitter = tf.getInstance (); 
		
		final List<Status> listaTweetsTotal = new ArrayList<>();
		
		final String cabecalhoMensagem = twitterProfessor + " tag: " + tagUtilizada + "\n";
		
		final LocalDate dataInicial = LocalDate.now().plusDays(1).minusWeeks(1);
		final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		final Map<String, List<Status>> mapaTweets = new HashMap<String, List<Status>>(7);
		
		final StringBuilder respostaQtdeTweets = new StringBuilder();
		final StringBuilder respostaReTweets = new StringBuilder();
		final StringBuilder respostaFavTweets = new StringBuilder();
		
		respostaQtdeTweets.append("Qtde tweets:\n");
		respostaReTweets.append("Qtde retweets:\n");
		respostaFavTweets.append("Qtde fav.:\n");
		
		for (LocalDate data = dataInicial; data.isBefore(LocalDate.now().plusDays(1)); data = data.plusDays(1)) {
			
			final String dataAtual = formatoData.format(data);
			
			Query query = new Query(tagUtilizada);
			
			query.setSince(dataAtual);
			
			query.setUntil(formatoData.format(data.plusDays(1)));
					
			QueryResult result = twitter.search(query);
			
			final List<Status> listaTweetsDia = new ArrayList<>(0);
			
			listaTweetsDia.addAll(result.getTweets());
			
			while(result.hasNext()){
				
				query = result.nextQuery();
				
				result = twitter.search(query);
				
				listaTweetsDia.addAll(result.getTweets());
				
			}
			
			mapaTweets.put(dataAtual, listaTweetsDia);
			
			//Quantidade de tweets do dia
			respostaQtdeTweets.append(data + " " + listaTweetsDia.size() + "\n");
			
			Integer qtdeReTweets = 0;
			Integer qtdeFavTweets = 0;
			
			for (Status tweet : listaTweetsDia) {
				
				//Conta quantos dos tweets são "reTweets"
				if (tweet.isRetweet()) {
					qtdeReTweets++;
				}
				
				//Quantidade de "favoritações" deste tweet, independente da data de "favoritação"
				qtdeFavTweets += tweet.getFavoriteCount();
				
			}
			
			//Resposta de quantidade de reTweets
			respostaReTweets.append(data + " " + qtdeReTweets + "\n");
			
			//Resposta de quantidade de "favoritações"
			respostaFavTweets.append(data + " " + qtdeFavTweets + "\n");
			
			listaTweetsTotal.addAll(listaTweetsDia);
			
		}
				
		//Ordena por nome do autor do tweet (não pelo código de usuário)
		
		final StringBuilder respostaNome = new StringBuilder();
		
		respostaNome.append("Autor, por nome:\n");
		
		listaTweetsTotal.sort(new OrdenadorStatusNome());
		
		final String primeiroNome = listaTweetsTotal.get(0).getUser().getName();
		
		respostaNome.append("Primeiro: " + primeiroNome + "\n");
		
		final String ultimoNome = listaTweetsTotal.get(listaTweetsTotal.size() - 1).getUser().getName();
		
		respostaNome.append("Último: " + ultimoNome + "\n");
		
		//Ordena por data de criação do tweet
		
		final StringBuilder respostaData =  new StringBuilder();
		
		respostaData.append("Data tweet:\n");
		
		listaTweetsTotal.sort(new OrdenadorStatusData());
		
		final Date primeiraData = listaTweetsTotal.get(0).getCreatedAt();
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		respostaData.append("Primeiro: " + sdf.format(primeiraData) + "\n");
		
		final Date ultimaData = listaTweetsTotal.get(listaTweetsTotal.size() - 1).getCreatedAt();
		
		respostaData.append("Último: " + sdf.format(ultimaData));
		
		twitter.updateStatus(cabecalhoMensagem + respostaQtdeTweets.toString());
		twitter.updateStatus(cabecalhoMensagem + respostaReTweets.toString());
		twitter.updateStatus(cabecalhoMensagem + respostaFavTweets.toString());
		twitter.updateStatus(cabecalhoMensagem + respostaNome.toString());
		twitter.updateStatus(cabecalhoMensagem + respostaData.toString());
		
	}
	
}
