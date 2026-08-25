package com.cabin.trivia

internal val airportCodesQuestions: List<Question> = listOf(
    Question(
        id = "sin",
        topic = Topic.AIRPORT_CODES,
        prompt = "What is the IATA code for Singapore Changi Airport?",
        choices = listOf("SGN", "SIN", "CGK", "KUL"),
        correctIndex = 1,
        explanation = "Singapore Changi Airport's IATA code is SIN; SGN is Ho Chi Minh City and CGK is Jakarta."
    ),
    Question(
        id = "ams",
        topic = Topic.AIRPORT_CODES,
        prompt = "Amsterdam Schiphol Airport uses which IATA code?",
        choices = listOf("AMS", "ARN", "BRU", "RTM"),
        correctIndex = 0,
        explanation = "Amsterdam Schiphol uses AMS. ARN is Stockholm Arlanda; RTM is Rotterdam."
    ),
    Question(
        id = "jfk",
        topic = Topic.AIRPORT_CODES,
        prompt = "JFK is the IATA code for which city's main international airport?",
        choices = listOf("Los Angeles", "Chicago", "New York", "Miami"),
        correctIndex = 2,
        explanation = "JFK is John F. Kennedy International in New York. Los Angeles is LAX; Miami is MIA."
    ),
        Question(
            id = "lhr",
            topic = Topic.AIRPORT_CODES,
            prompt = "What is the IATA code for London Heathrow Airport?",
            choices = listOf("LGW", "LHR", "STN", "LCY"),
            correctIndex = 1,
            explanation = "Heathrow is LHR. LGW is Gatwick, STN is Stansted, and LCY is London City."
        ),
        Question(
            id = "cdg",
            topic = Topic.AIRPORT_CODES,
            prompt = "Paris Charles de Gaulle Airport uses which IATA code?",
            choices = listOf("ORY", "BVA", "CDG", "PAR"),
            correctIndex = 2,
            explanation = "Charles de Gaulle is CDG. ORY is Orly; PAR is a city code, not a single airport."
        ),
        Question(
            id = "dxb",
            topic = Topic.AIRPORT_CODES,
            prompt = "Dubai International Airport's IATA code is:",
            choices = listOf("AUH", "DXB", "DWC", "SHJ"),
            correctIndex = 1,
            explanation = "Dubai International is DXB. DWC is Al Maktoum; AUH is Abu Dhabi; SHJ is Sharjah."
        ),
        Question(
            id = "hnd",
            topic = Topic.AIRPORT_CODES,
            prompt = "Tokyo Haneda Airport uses which IATA code?",
            choices = listOf("NRT", "HND", "KIX", "NGO"),
            correctIndex = 1,
            explanation = "Haneda is HND. NRT is Narita; KIX is Kansai (Osaka)."
        ),
        Question(
            id = "nrt",
            topic = Topic.AIRPORT_CODES,
            prompt = "Narita International Airport, serving Tokyo, is coded:",
            choices = listOf("HND", "NRT", "ITM", "CTS"),
            correctIndex = 1,
            explanation = "Narita is NRT. HND is Haneda, closer to central Tokyo."
        ),
        Question(
            id = "lax",
            topic = Topic.AIRPORT_CODES,
            prompt = "Los Angeles International Airport's IATA code is:",
            choices = listOf("LAX", "BUR", "SNA", "LGB"),
            correctIndex = 0,
            explanation = "Los Angeles International is LAX. BUR is Burbank; SNA is John Wayne (Orange County)."
        ),
        Question(
            id = "ord",
            topic = Topic.AIRPORT_CODES,
            prompt = "Chicago O'Hare International Airport uses which IATA code?",
            choices = listOf("MDW", "ORD", "CHI", "RFD"),
            correctIndex = 1,
            explanation = "O'Hare is ORD (from the old Orchard Field name). MDW is Midway."
        ),
        Question(
            id = "atl",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hartsfield-Jackson Atlanta International Airport is coded:",
            choices = listOf("ATL", "SAV", "PDK", "AHN"),
            correctIndex = 0,
            explanation = "Hartsfield-Jackson Atlanta is ATL. PDK is DeKalb-Peachtree."
        ),
        Question(
            id = "syd",
            topic = Topic.AIRPORT_CODES,
            prompt = "Sydney Kingsford Smith Airport's IATA code is:",
            choices = listOf("MEL", "BNE", "SYD", "CBR"),
            correctIndex = 2,
            explanation = "Sydney is SYD. MEL is Melbourne; BNE is Brisbane; CBR is Canberra."
        ),
        Question(
            id = "hkg",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hong Kong International Airport uses which IATA code?",
            choices = listOf("HKG", "HHP", "MFM", "SZX"),
            correctIndex = 0,
            explanation = "Hong Kong International is HKG. MFM is Macau; SZX is Shenzhen."
        ),
        Question(
            id = "icn",
            topic = Topic.AIRPORT_CODES,
            prompt = "Seoul Incheon International Airport is coded:",
            choices = listOf("GMP", "ICN", "PUS", "CJU"),
            correctIndex = 1,
            explanation = "Incheon is ICN. GMP is Gimpo, closer to central Seoul."
        ),
        Question(
            id = "bkk",
            topic = Topic.AIRPORT_CODES,
            prompt = "Bangkok Suvarnabhumi Airport's IATA code is:",
            choices = listOf("DMK", "BKK", "CNX", "HKT"),
            correctIndex = 1,
            explanation = "Suvarnabhumi is BKK. DMK is Don Mueang, Bangkok's older airport."
        ),
        Question(
            id = "mad",
            topic = Topic.AIRPORT_CODES,
            prompt = "Madrid-Barajas Adolfo Suárez Airport uses which IATA code?",
            choices = listOf("BCN", "MAD", "AGP", "VLC"),
            correctIndex = 1,
            explanation = "Madrid-Barajas is MAD. BCN is Barcelona-El Prat."
        ),
        Question(
            id = "fco",
            topic = Topic.AIRPORT_CODES,
            prompt = "Rome Fiumicino Airport's IATA code is:",
            choices = listOf("CIA", "FCO", "NAP", "VCE"),
            correctIndex = 1,
            explanation = "Fiumicino is FCO. CIA is Ciampino, Rome's secondary airport."
        ),
        Question(
            id = "doh",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hamad International Airport in Doha is coded:",
            choices = listOf("DIA", "DOH", "OTA", "BAH"),
            correctIndex = 1,
            explanation = "Doha's Hamad International is DOH. BAH is Bahrain."
        ),
        Question(
            id = "gru",
            topic = Topic.AIRPORT_CODES,
            prompt = "São Paulo-Guarulhos International Airport uses which IATA code?",
            choices = listOf("CGH", "VCP", "GRU", "GIG"),
            correctIndex = 2,
            explanation = "Guarulhos is GRU. CGH is Congonhas; GIG is Rio Galeão."
        ),
        Question(
            id = "eze",
            topic = Topic.AIRPORT_CODES,
            prompt = "Buenos Aires Ministro Pistarini (Ezeiza) Airport is coded:",
            choices = listOf("AEP", "EZE", "COR", "MVD"),
            correctIndex = 1,
            explanation = "Ezeiza is EZE. AEP is Aeroparque Jorge Newbery, closer to downtown."
        ),
        Question(
            id = "mex",
            topic = Topic.AIRPORT_CODES,
            prompt = "Mexico City International Airport's IATA code is:",
            choices = listOf("MEX", "NLU", "CUN", "GDL"),
            correctIndex = 0,
            explanation = "Mexico City International (Benito Juárez) is MEX. NLU is Felipe Ángeles; CUN is Cancún."
        ),
        Question(
            id = "cph",
            topic = Topic.AIRPORT_CODES,
            prompt = "Copenhagen Airport (Kastrup) uses which IATA code?",
            choices = listOf("ARN", "OSL", "CPH", "BLL"),
            correctIndex = 2,
            explanation = "Copenhagen Kastrup is CPH. ARN is Stockholm Arlanda; OSL is Oslo."
        ),
        Question(
            id = "zrh",
            topic = Topic.AIRPORT_CODES,
            prompt = "Zurich Airport's IATA code is:",
            choices = listOf("GVA", "BSL", "BRN", "ZRH"),
            correctIndex = 3,
            explanation = "Zurich is ZRH. GVA is Geneva; BSL is EuroAirport Basel."
        ),
)
