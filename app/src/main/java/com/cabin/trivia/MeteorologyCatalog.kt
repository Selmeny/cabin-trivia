package com.cabin.trivia

internal val meteorologyQuestions: List<Question> = listOf(
    Question(
        id = "contrail",
        topic = Topic.METEOROLOGY,
        prompt = "The white trails often seen behind jets at cruise altitude are called:",
        choices = listOf("Wake vortices", "Contrails", "St. Elmo's fire", "Chemtrails (a scientific term)"),
        correctIndex = 1,
        explanation = "Contrails are condensation trails from engine exhaust freezing in cold, humid air at altitude."
    ),
    Question(
        id = "cb",
        topic = Topic.METEOROLOGY,
        prompt = "Which cloud type is most associated with thunderstorms and severe turbulence?",
        choices = listOf("Cirrus", "Stratus", "Cumulonimbus", "Altocumulus"),
        correctIndex = 2,
        explanation = "Cumulonimbus clouds bring thunderstorms, hail, and severe turbulence. Cirrus are high and thin."
    ),
        Question(
            id = "jet-stream",
            topic = Topic.METEOROLOGY,
            prompt = "The jet stream is best described as:",
            choices = listOf("A surface trade wind", "A narrow band of strong winds near the tropopause", "A thunderstorm outflow", "A mountain breeze"),
            correctIndex = 1,
            explanation = "Jet streams are fast, narrow west-to-east winds near the tropopause that airliners use or avoid."
        ),
        Question(
            id = "cat",
            topic = Topic.METEOROLOGY,
            prompt = "Clear-air turbulence (CAT) typically occurs:",
            choices = listOf("Only inside thunderstorms", "In cloud-free air, often near jet streams", "Only below 10,000 feet", "Only over oceans"),
            correctIndex = 1,
            explanation = "CAT is turbulence without nearby clouds, often in shear near the jet stream."
        ),
        Question(
            id = "wind-shear",
            topic = Topic.METEOROLOGY,
            prompt = "Wind shear is:",
            choices = listOf("Steady headwind on final", "A sudden change in wind speed or direction", "Only a high-altitude phenomenon", "The same as wake turbulence"),
            correctIndex = 1,
            explanation = "Shear is an abrupt wind-vector change; low-level shear on approach is especially dangerous."
        ),
        Question(
            id = "microburst",
            topic = Topic.METEOROLOGY,
            prompt = "A microburst is:",
            choices = listOf("A small tornado", "An intense, localized downdraft", "A type of icing", "A jet-stream core"),
            correctIndex = 1,
            explanation = "A microburst is a strong, short-lived downdraft that can cause a sudden performance loss on takeoff or landing."
        ),
        Question(
            id = "icing",
            topic = Topic.METEOROLOGY,
            prompt = "Airframe icing is most likely when flying through:",
            choices = listOf("Dry snow", "Supercooled liquid water droplets", "Pure ice crystals at −60°C", "Warm rain above 10°C"),
            correctIndex = 1,
            explanation = "Supercooled droplets freeze on impact. Very cold ice-crystal clouds often produce less accretion."
        ),
        Question(
            id = "wake-turb",
            topic = Topic.METEOROLOGY,
            prompt = "Wake turbulence behind a heavy jet is mainly caused by:",
            choices = listOf("Engine exhaust only", "Wingtip vortices from lift", "Landing-gear drag", "Spoilers"),
            correctIndex = 1,
            explanation = "Lift produces counter-rotating wingtip vortices; they sink and persist, especially behind heavy, clean aircraft."
        ),
        Question(
            id = "tropopause",
            topic = Topic.METEOROLOGY,
            prompt = "The tropopause is the boundary between:",
            choices = listOf("Troposphere and stratosphere", "Stratosphere and mesosphere", "Boundary layer and free atmosphere", "Ionosphere and space"),
            correctIndex = 0,
            explanation = "The tropopause caps the troposphere; jet streams and much cruise traffic sit near it."
        ),
        Question(
            id = "isa-15",
            topic = Topic.METEOROLOGY,
            prompt = "ISA sea-level temperature is:",
            choices = listOf("0°C", "15°C", "20°C", "59°C"),
            correctIndex = 1,
            explanation = "The International Standard Atmosphere uses 15°C (59°F) and 1013.25 hPa at mean sea level."
        ),
        Question(
            id = "qnh",
            topic = Topic.METEOROLOGY,
            prompt = "When an altimeter is set to QNH, it reads approximately:",
            choices = listOf("Height above the airfield when on the ground", "Altitude above mean sea level", "Flight level", "Cabin altitude"),
            correctIndex = 1,
            explanation = "QNH is the pressure setting so the altimeter shows elevation AMSL on the ground at that airport."
        ),
        Question(
            id = "metar",
            topic = Topic.METEOROLOGY,
            prompt = "A METAR is:",
            choices = listOf("A 24-hour aerodrome forecast", "A routine aviation weather observation", "A volcanic-ash advisory", "A NOTAM"),
            correctIndex = 1,
            explanation = "METARs are coded routine observations; TAFs are forecasts."
        ),
        Question(
            id = "taf",
            topic = Topic.METEOROLOGY,
            prompt = "A TAF is:",
            choices = listOf("A terminal aerodrome forecast", "A takeoff alternate formula", "A turbulence advisory format", "A taxiway assignment"),
            correctIndex = 0,
            explanation = "TAF means Terminal Aerodrome Forecast, issued for a specific airport over a period of hours."
        ),
        Question(
            id = "rvr",
            topic = Topic.METEOROLOGY,
            prompt = "RVR stands for:",
            choices = listOf("Radar velocity range", "Runway visual range", "Regional vortex report", "Required visibility rating"),
            correctIndex = 1,
            explanation = "Runway visual range is the distance a pilot can see down the runway from the threshold."
        ),
        Question(
            id = "mountain-wave",
            topic = Topic.METEOROLOGY,
            prompt = "Mountain wave turbulence is found:",
            choices = listOf("Only on the windward slope at ground level", "Downwind of ridges in stable flow, sometimes far downstream", "Only in the tropics", "Only inside cumulonimbus"),
            correctIndex = 1,
            explanation = "Waves and rotors form in the lee of mountains when a stable airstream is forced over terrain."
        ),
        Question(
            id = "itcz",
            topic = Topic.METEOROLOGY,
            prompt = "The ITCZ is:",
            choices = listOf("A polar front", "The Intertropical Convergence Zone of thunderstorms near the equator", "A North Atlantic jet", "A type of icing"),
            correctIndex = 1,
            explanation = "Trade winds converge near the equator in the ITCZ, producing widespread convective weather."
        ),
        Question(
            id = "dewpoint",
            topic = Topic.METEOROLOGY,
            prompt = "Dewpoint is the temperature at which:",
            choices = listOf("Water boils at altitude", "Air becomes saturated if cooled at constant pressure", "Jet fuel freezes", "ISA temperature is defined"),
            correctIndex = 1,
            explanation = "When temperature equals dewpoint, relative humidity is 100% and fog or cloud can form."
        ),
        Question(
            id = "std-pressure",
            topic = Topic.METEOROLOGY,
            prompt = "Standard sea-level pressure used for flight levels is:",
            choices = listOf("29.92 inHg / 1013.25 hPa", "28.92 inHg", "30.50 inHg", "1000.00 hPa exactly"),
            correctIndex = 0,
            explanation = "Above the transition altitude, altimeters are set to 29.92 inHg (1013.25 hPa) to fly flight levels."
        ),
        Question(
            id = "virga",
            topic = Topic.METEOROLOGY,
            prompt = "Virga is:",
            choices = listOf("Hail on the runway", "Precipitation that evaporates before reaching the ground", "A type of lightning", "Freezing rain"),
            correctIndex = 1,
            explanation = "Virga can still produce dangerous downdrafts even if rain does not reach the surface."
        ),
        Question(
            id = "inversion",
            topic = Topic.METEOROLOGY,
            prompt = "A temperature inversion is a layer where:",
            choices = listOf("Temperature decreases rapidly with height", "Temperature increases with height", "Wind is always calm", "Clouds cannot form"),
            correctIndex = 1,
            explanation = "In an inversion, warmer air sits above cooler air; it can trap pollutants and affect climb performance."
        ),
        Question(
            id = "advection-fog",
            topic = Topic.METEOROLOGY,
            prompt = "Advection fog commonly forms when:",
            choices = listOf("Warm moist air moves over a colder surface", "The ground radiates heat on a clear night only", "A thunderstorm outflows", "Jet stream cirrus thickens"),
            correctIndex = 0,
            explanation = "Advection fog is typical when warm, moist air streams over a cold sea or snow-covered ground."
        ),
        Question(
            id = "hail",
            topic = Topic.METEOROLOGY,
            prompt = "Hail that can damage aircraft is produced mainly in:",
            choices = listOf("Stratus", "Cumulonimbus with strong updrafts", "Cirrus", "Fog"),
            correctIndex = 1,
            explanation = "Strong CB updrafts recycle supercooled water until ice pellets grow large enough to fall as hail."
        ),
)
