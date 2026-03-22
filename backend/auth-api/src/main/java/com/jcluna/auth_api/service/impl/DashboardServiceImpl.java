package com.jcluna.auth_api.service.impl;


import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DashboardServiceImpl implements DashboardService {


    private final List<QuoteResponse> frases = List.of(
            new QuoteResponse("El secreto para anvanzar es comenzar.","Mark Twain"),
            new QuoteResponse("Caminante, no hay camino, se hace camino al andar.","Antonio Machado"),
            new QuoteResponse("No importa lo lento que vayas, siempre que no te detengas.","Confucio"),
            new QuoteResponse("El trabajo y la constancia son la base de todo progreso.","Benito Pérez Galdós"),
            new QuoteResponse("La perseverancia es fallar 19 veces y acertar la 20.","Julie Andrews"),
            new QuoteResponse("Cada día es una nueva oportunidad.","Jorge Luis Borges")
    );


    private final Random random = new Random();


    @Override
    public QuoteResponse getRandomQuote() {
        int index = random.nextInt(frases.size());
        return frases.get(index);
    }
}
