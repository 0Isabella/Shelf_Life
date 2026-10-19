package com.example.shelflife.data

import com.example.shelflife.R
import com.example.shelflife.model.Livro

val LivrosIniciais = listOf(
    Livro(
        id = 0,
        titulo = "Enterrem Nossos Ossos à Meia-Noite",
        autor = "V.E. Schwab",
        imagemRes = R.drawable.enterrem_nossos_ossos,
        paginasTotal = 664,
        paginasLidas = 328,
        minutosLidos = 439,
        sinopse = "Esta é uma história sobre sede. 1532. Santo Domingo de la Calzada. Maria sempre foi uma menina de personalidade forte. Cresceu cheia de sonhos e com uma beleza que chamava a atenção. Mas ela sabe que no jogo ditado pelos homens, só lhe será permitido um papel: troféu ou peão. Quando uma misteriosa viajante aparece por Maria uma fuga da vida que a garota tanto despreza, ela faz uma escolha desesperada. E jura nunca se arrepender de nada. Esta é uma história sobre amor. 1837. Londres. A jovem Calla é solidária na propriedade da família no campo, até que a sua vida desmorona ao ser flagrada em um momento íntimo proibido e, como consequência, é enviada a Londres. Lá, seus corações solitários e seus desejos mais audaciosos ficam extremamente quando uma bela viúva lhe faz uma proposta. Ela só não imaginava que o preço da liberdade seria tão alto."
    ),
    Livro(1, "Um Conto Para Ser Tempo", autor = "Ruth Ozeki", imagemRes = R.drawable.um_conto_para_ser_tempo,
        paginasTotal = 320, paginasLidas = 120, minutosLidos = 180),
    Livro(2, "1984", autor = "George Orwell", imagemRes = R.drawable.livro_1984,
        paginasTotal = 416, paginasLidas = 416, minutosLidos = 780),
    Livro(3, "O Sol e a Estrela", autor = "Rick Riordan e Mark Oshiro", imagemRes = R.drawable.o_sol_e_a_estrela,
        paginasTotal = 480, paginasLidas = 200, minutosLidos = 300),
    Livro(4, "Labirinto do Fauno", autor = "Guilhermo del Toro e Cornelia Funke", imagemRes = R.drawable.labirinto_do_fauno,
        paginasTotal = 300, paginasLidas = 90, minutosLidos = 140),
    Livro(5, "O Código Da Vinci", autor = "Dan Brown", imagemRes = R.drawable.o_codigo_da_vinci,
        paginasTotal = 480, paginasLidas = 480, minutosLidos = 700),
    Livro(6, "1793", autor = "Niklas Natt och Dag"),
    Livro(7, "Ponto de Vista do Leitor Onisciente", autor = "singNsong"),
    Livro(8, "A Vida Invisível de Addie LaRue", autor = "V.E. Schwab"),
    Livro(9, "Battle Royale", autor = "Koushun Takami"),
    Livro(10, "Noite na Taverna", autor = "Álvares de Azevedo"),
    Livro(11, "Assassinato Express do Oriente", autor = "Agatha Christie")
)