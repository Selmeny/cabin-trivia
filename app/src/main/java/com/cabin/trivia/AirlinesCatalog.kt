package com.cabin.trivia

internal val airlinesQuestions: List<Question> = listOf(
    Question(
        id = "qantas",
        topic = Topic.AIRLINES,
        prompt = "Which airline's livery features a kangaroo on the tail?",
        choices = listOf("Air New Zealand", "Qantas", "Emirates", "Cathay Pacific"),
        correctIndex = 1,
        explanation = "Qantas (Australia) paints a kangaroo on the tail. Air New Zealand uses a koru."
    ),
    Question(
        id = "ana",
        topic = Topic.AIRLINES,
        prompt = "ANA is the IATA code for which airline?",
        choices = listOf("Air New Zealand", "Austrian Airlines", "All Nippon Airways", "Air North"),
        correctIndex = 2,
        explanation = "ANA stands for All Nippon Airways, Japan's largest airline by fleet."
    ),
    Question(
        id = "lufthansa",
        topic = Topic.AIRLINES,
        prompt = "Lufthansa's main hub is which airport?",
        choices = listOf("Munich (MUC)", "Berlin Brandenburg (BER)", "Frankfurt (FRA)", "Düsseldorf (DUS)"),
        correctIndex = 2,
        explanation = "Lufthansa's primary hub is Frankfurt (FRA). Munich (MUC) is the second hub."
    ),
        Question(
            id = "emirates-ek",
            topic = Topic.AIRLINES,
            prompt = "What is Emirates' IATA airline code?",
            choices = listOf("QR", "EY", "EK", "FZ"),
            correctIndex = 2,
            explanation = "Emirates is EK. QR is Qatar Airways; EY is Etihad; FZ is flydubai."
        ),
        Question(
            id = "sq",
            topic = Topic.AIRLINES,
            prompt = "Singapore Airlines uses which IATA code?",
            choices = listOf("SQ", "MI", "TR", "3K"),
            correctIndex = 0,
            explanation = "Singapore Airlines is SQ. MI is SilkAir (now merged); TR is Scoot."
        ),
        Question(
            id = "ba",
            topic = Topic.AIRLINES,
            prompt = "British Airways' IATA code is:",
            choices = listOf("VS", "BA", "BD", "U2"),
            correctIndex = 1,
            explanation = "British Airways is BA. VS is Virgin Atlantic; U2 is easyJet."
        ),
        Question(
            id = "af",
            topic = Topic.AIRLINES,
            prompt = "Air France uses which IATA code?",
            choices = listOf("AF", "TO", "SS", "XK"),
            correctIndex = 0,
            explanation = "Air France is AF. It is in a combined group with KLM."
        ),
        Question(
            id = "kl",
            topic = Topic.AIRLINES,
            prompt = "KLM Royal Dutch Airlines' IATA code is:",
            choices = listOf("KL", "HV", "OR", "WA"),
            correctIndex = 0,
            explanation = "KLM is KL (Koninklijke Luchtvaart Maatschappij). HV is Transavia."
        ),
        Question(
            id = "ua",
            topic = Topic.AIRLINES,
            prompt = "United Airlines uses which IATA code?",
            choices = listOf("DL", "AA", "UA", "WN"),
            correctIndex = 2,
            explanation = "United is UA. DL is Delta; AA is American; WN is Southwest."
        ),
        Question(
            id = "dl",
            topic = Topic.AIRLINES,
            prompt = "Delta Air Lines' IATA code is:",
            choices = listOf("DL", "DA", "DE", "NW"),
            correctIndex = 0,
            explanation = "Delta is DL. NW was Northwest, which merged into Delta."
        ),
        Question(
            id = "aa",
            topic = Topic.AIRLINES,
            prompt = "American Airlines uses which IATA code?",
            choices = listOf("AM", "AA", "AS", "B6"),
            correctIndex = 1,
            explanation = "American Airlines is AA. AM is Aeroméxico; AS is Alaska; B6 is JetBlue."
        ),
        Question(
            id = "wn",
            topic = Topic.AIRLINES,
            prompt = "Southwest Airlines' IATA code is:",
            choices = listOf("SW", "WN", "NK", "F9"),
            correctIndex = 1,
            explanation = "Southwest is WN (from the former AirTran/company history of 'WN'). NK is Spirit; F9 is Frontier."
        ),
        Question(
            id = "fr",
            topic = Topic.AIRLINES,
            prompt = "Ryanair uses which IATA code?",
            choices = listOf("FR", "RK", "U2", "W6"),
            correctIndex = 0,
            explanation = "Ryanair is FR. U2 is easyJet; W6 is Wizz Air."
        ),
        Question(
            id = "cx",
            topic = Topic.AIRLINES,
            prompt = "Cathay Pacific's IATA code is:",
            choices = listOf("KA", "CX", "HX", "UO"),
            correctIndex = 1,
            explanation = "Cathay Pacific is CX. KA was Cathay Dragon; HX is Hong Kong Airlines."
        ),
        Question(
            id = "jl",
            topic = Topic.AIRLINES,
            prompt = "Japan Airlines uses which IATA code?",
            choices = listOf("NH", "JL", "NQ", "MM"),
            correctIndex = 1,
            explanation = "Japan Airlines is JL. NH is ANA."
        ),
        Question(
            id = "ke",
            topic = Topic.AIRLINES,
            prompt = "Korean Air's IATA code is:",
            choices = listOf("OZ", "KE", "LJ", "ZE"),
            correctIndex = 1,
            explanation = "Korean Air is KE. OZ is Asiana."
        ),
        Question(
            id = "tk",
            topic = Topic.AIRLINES,
            prompt = "Turkish Airlines uses which IATA code?",
            choices = listOf("TK", "PC", "XQ", "KK"),
            correctIndex = 0,
            explanation = "Turkish Airlines is TK. PC is Pegasus."
        ),
        Question(
            id = "qr",
            topic = Topic.AIRLINES,
            prompt = "Qatar Airways' IATA code is:",
            choices = listOf("EK", "QR", "EY", "GF"),
            correctIndex = 1,
            explanation = "Qatar Airways is QR. EK is Emirates; EY is Etihad; GF is Gulf Air."
        ),
        Question(
            id = "ey",
            topic = Topic.AIRLINES,
            prompt = "Etihad Airways uses which IATA code?",
            choices = listOf("EY", "EK", "WY", "SV"),
            correctIndex = 0,
            explanation = "Etihad (Abu Dhabi) is EY. EK is Emirates; SV is Saudia."
        ),
        Question(
            id = "ac",
            topic = Topic.AIRLINES,
            prompt = "Which airline's livery features a maple leaf?",
            choices = listOf("Air France", "Air Canada", "Lufthansa", "Qantas"),
            correctIndex = 1,
            explanation = "Air Canada's livery uses a maple leaf. Its IATA code is AC."
        ),
        Question(
            id = "sk",
            topic = Topic.AIRLINES,
            prompt = "SAS (Scandinavian Airlines) uses which IATA code?",
            choices = listOf("SK", "DY", "D8", "WF"),
            correctIndex = 0,
            explanation = "SAS is SK. DY is Norwegian; WF is Widerøe."
        ),
        Question(
            id = "et",
            topic = Topic.AIRLINES,
            prompt = "Ethiopian Airlines' IATA code is:",
            choices = listOf("ET", "MS", "SA", "KQ"),
            correctIndex = 0,
            explanation = "Ethiopian is ET. MS is EgyptAir; SA is South African; KQ is Kenya Airways."
        ),
        Question(
            id = "ib",
            topic = Topic.AIRLINES,
            prompt = "Iberia uses which IATA code?",
            choices = listOf("UX", "IB", "VY", "I2"),
            correctIndex = 1,
            explanation = "Iberia is IB. VY is Vueling; UX is Air Europa."
        ),
)
