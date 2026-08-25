package com.cabin.trivia

internal val famousFlightsQuestions: List<Question> = listOf(
    Question(
        id = "spirit",
        topic = Topic.FAMOUS_FLIGHTS,
        prompt = "Who piloted the Spirit of St. Louis on the first solo nonstop transatlantic flight?",
        choices = listOf("Amelia Earhart", "Charles Lindbergh", "Wiley Post", "Howard Hughes"),
        correctIndex = 1,
        explanation = "Charles Lindbergh flew the Spirit of St. Louis from New York to Paris in 1927."
    ),
    Question(
        id = "af1",
        topic = Topic.FAMOUS_FLIGHTS,
        prompt = "Air Force One is the call sign used when which passenger is aboard a U.S. Air Force aircraft?",
        choices = listOf("The Vice President", "The Secretary of Defense", "The President of the United States", "The Speaker of the House"),
        correctIndex = 2,
        explanation = "Air Force One is the call sign for a U.S. Air Force aircraft carrying the President."
    ),
        Question(
            id = "wright-1903",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The Wright brothers' first powered flight at Kitty Hawk was in which year?",
            choices = listOf("1899", "1903", "1908", "1914"),
            correctIndex = 1,
            explanation = "Orville Wright flew the Wright Flyer on 17 December 1903 at Kitty Hawk, North Carolina."
        ),
        Question(
            id = "earhart-1937",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Amelia Earhart disappeared in 1937 during an attempt to:",
            choices = listOf("Cross the Atlantic solo", "Fly around the world", "Reach the South Pole", "Break the sound barrier"),
            correctIndex = 1,
            explanation = "Earhart and navigator Fred Noonan vanished over the central Pacific on a world-flight attempt."
        ),
        Question(
            id = "yeager-x1",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Who first broke the sound barrier in level flight, in the Bell X-1?",
            choices = listOf("Neil Armstrong", "Chuck Yeager", "John Glenn", "Scott Crossfield"),
            correctIndex = 1,
            explanation = "Chuck Yeager exceeded Mach 1 on 14 October 1947 in the Bell X-1 Glamorous Glennis."
        ),
        Question(
            id = "alcock-brown",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The first nonstop transatlantic flight (1919) was flown by:",
            choices = listOf("Lindbergh", "Alcock and Brown", "Earhart", "the Wright brothers"),
            correctIndex = 1,
            explanation = "John Alcock and Arthur Brown flew a Vickers Vimy from Newfoundland to Ireland in June 1919."
        ),
        Question(
            id = "bleriot-1909",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Louis Blériot is famous for the first airplane crossing of:",
            choices = listOf("The Atlantic", "The English Channel", "The Mediterranean", "The Alps"),
            correctIndex = 1,
            explanation = "Blériot crossed the English Channel from Calais to Dover on 25 July 1909."
        ),
        Question(
            id = "hudson-1549",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "US Airways Flight 1549 in 2009 ditched on which river after a bird strike?",
            choices = listOf("The East River", "The Hudson River", "The Potomac", "The Delaware"),
            correctIndex = 1,
            explanation = "Chesley Sullenberger ditched in the Hudson River off Manhattan; all 155 aboard survived."
        ),
        Question(
            id = "gimli-143",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The 'Gimli Glider' was which Air Canada flight that ran out of fuel in 1983?",
            choices = listOf("Flight 621", "Flight 143", "Flight 797", "Flight 189"),
            correctIndex = 1,
            explanation = "Air Canada 143, a 767, glided to a landing at Gimli, Manitoba, after a metric fuel-loading error."
        ),
        Question(
            id = "tenerife-1977",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The 1977 Tenerife disaster involved a runway collision between KLM and which other airline?",
            choices = listOf("TWA", "Pan Am", "British Airways", "Iberia"),
            correctIndex = 1,
            explanation = "KLM 4805 and Pan Am 1736 collided on the runway at Los Rodeos; it remains the deadliest accident in aviation."
        ),
        Question(
            id = "concorde-2003",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Concorde's last commercial passenger flights were in which year?",
            choices = listOf("1999", "2001", "2003", "2006"),
            correctIndex = 2,
            explanation = "Air France and British Airways retired Concorde in 2003 after the 2000 Paris crash and rising costs."
        ),
        Question(
            id = "voyager-1986",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The first unrefueled nonstop flight around the world (1986) was in the aircraft named:",
            choices = listOf("Spirit of St. Louis", "Voyager", "GlobalFlyer", "Graf Zeppelin"),
            correctIndex = 1,
            explanation = "Dick Rutan and Jeana Yeager flew the Rutan Voyager around the world in nine days in December 1986."
        ),
        Question(
            id = "hindenburg-1937",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The Hindenburg airship disaster in 1937 occurred at:",
            choices = listOf("Lakehurst, New Jersey", "Friedrichshafen", "Cardington", "Akron"),
            correctIndex = 0,
            explanation = "LZ 129 Hindenburg caught fire while landing at Lakehurst on 6 May 1937."
        ),
        Question(
            id = "panam-103",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Pan Am Flight 103 was destroyed by a bomb over which town in 1988?",
            choices = listOf("Lockerbie", "Lockerbie's neighbor Moffat", "Prestwick", "Shannon"),
            correctIndex = 0,
            explanation = "The 747 was destroyed over Lockerbie, Scotland, on 21 December 1988."
        ),
        Question(
            id = "ual-232",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "United Airlines Flight 232 (1989) lost all hydraulics and landed at:",
            choices = listOf("Sioux City, Iowa", "Denver", "Chicago O'Hare", "Minneapolis"),
            correctIndex = 0,
            explanation = "After an uncontained engine failure, the DC-10 was crash-landed at Sioux Gateway Airport."
        ),
        Question(
            id = "qf32",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Qantas Flight 32 in 2010 was an A380 that suffered an uncontained engine failure after departing:",
            choices = listOf("Sydney", "Singapore", "London", "Los Angeles"),
            correctIndex = 1,
            explanation = "QF32 left Singapore for Sydney; the crew returned to Singapore after a Trent 900 failure."
        ),
        Question(
            id = "ba009-ash",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "British Airways Flight 009 in 1982 lost all four engines after flying through:",
            choices = listOf("A hailstorm", "Volcanic ash", "Severe icing", "A microburst"),
            correctIndex = 1,
            explanation = "The 747 flew through ash from Mount Galunggung; engines were restarted and it landed at Jakarta."
        ),
        Question(
            id = "kal-007",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Korean Air Lines Flight 007 was shot down in 1983 after straying into which country's airspace?",
            choices = listOf("China", "the Soviet Union", "North Korea", "Japan"),
            correctIndex = 1,
            explanation = "KAL 007 was shot down by a Soviet fighter over Sakhalin after a navigation error."
        ),
        Question(
            id = "db-cooper",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "D. B. Cooper is known for hijacking a Northwest Orient 727 in 1971 and then:",
            choices = listOf("Landing in Cuba", "Parachuting from the aircraft", "Crashing in the Rockies", "Surrendering in Seattle"),
            correctIndex = 1,
            explanation = "After exchanging passengers for ransom in Seattle, Cooper jumped from the 727 over the Pacific Northwest and was never found."
        ),
        Question(
            id = "flight-19",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Flight 19 was a 1945 U.S. Navy trainer formation that disappeared over:",
            choices = listOf("The Bermuda Triangle area of the Atlantic", "the Pacific off California", "Lake Michigan", "the North Sea"),
            correctIndex = 0,
            explanation = "Five TBM Avengers vanished east of Florida; a search plane was also lost."
        ),
        Question(
            id = "sts-107",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Space Shuttle Columbia was lost during reentry on mission:",
            choices = listOf("STS-51-L", "STS-107", "STS-1", "STS-135"),
            correctIndex = 1,
            explanation = "STS-107 broke up over Texas on 1 February 2003. STS-51-L was Challenger."
        ),
        Question(
            id = "earhart-1932",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Who was the first woman to fly solo nonstop across the Atlantic (1932)?",
            choices = listOf("Bessie Coleman", "Amelia Earhart", "Amy Johnson", "Harriet Quimby"),
            correctIndex = 1,
            explanation = "Earhart flew a Lockheed Vega from Newfoundland to Northern Ireland in May 1932."
        ),
)
