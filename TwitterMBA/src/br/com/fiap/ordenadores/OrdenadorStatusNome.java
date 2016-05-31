package br.com.fiap.ordenadores;

import java.util.Comparator;

import twitter4j.Status;

public class OrdenadorStatusNome implements Comparator<Status> {

	@Override
	public int compare(Status tweet1, Status tweet2) {
		return tweet1.getUser().getName().compareTo(tweet2.getUser().getName());
	}
}
