package br.com.fiap.ordenadores;

import java.util.Comparator;

import twitter4j.Status;

public class OrdenadorStatusData implements Comparator<Status> {

	@Override
	public int compare(Status tweet1, Status tweet2) {
		return tweet1.getCreatedAt().compareTo(tweet2.getCreatedAt());
	}

}
