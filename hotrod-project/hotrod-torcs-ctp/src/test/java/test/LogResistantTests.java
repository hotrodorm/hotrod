package test;

import java.io.File;
import java.io.IOException;

import org.hotrod.torcs.ctp.LogResistantFormatter;
import org.hotrod.torcs.ctp.LogResistantFormatter.InvalidLogDataException;

import test.FileUtils.CouldNotReadFromFileException;

public class LogResistantTests {

  public static void main(final String[] args)
      throws IOException, CouldNotReadFromFileException, InvalidLogDataException {

//    render();
    parse();
  }

  private static void render() throws CouldNotReadFromFileException, IOException {
    String plan = new String(FileUtils.readFromFile(new File("docs/plan3.txt")));
    System.out.println("--- Plan (orig) ---\n" + plan + "\n\n");

    LogResistantFormatter f = new LogResistantFormatter();
    String[] lines = f.render(plan);

    System.out.println("--- Plan (" + lines.length + " lines) ---");
    for (String l : lines) {
      System.out.println(l);
    }
    System.out.println("--- end of plan ---");
  }

  private static void parse() throws InvalidLogDataException {
//    String[] logLines = { " aska{1/3:abc}__ {3/3:ghi}s", "{2/3:def}" };
//    String[] logLines = { "{1/1:ASEA3v9hypeBEsobvcr6wjGzmiPcTaeG7/gUfE5yuYB3ha/uSLs=}" };

//    String[] logLines = {
//        "{1/17:1ZrNbttGEMfveYoFgUB2kRCiJMuS0LRw7QROatiplaAH1xAIcQ0ToUWFXCE2AucFcuk1OfcN0ifoueitxx576KWnPkA5K5HUfpA7S7ktOoBtkDuzXM7/N7tL}",
//        "{3/17:/TYM2GV2utPtlxfKRkW+pjfZ6TNn6oaB84A4cfaXHDwe78NBujoZwt9zoUe4xtnqBClumzcLt35MU0YDchTH8yIDy06ye58xckojn4XxLL0M5+B/smA0UTxr}",
//        "{2/17:0mf3CHmb/RDiPI/8mTNaHWXHx3FAyYubOc1OOuM4Yc6DvOm5n/hRRCOy98ZPoP3Cj1JaNI+Zn7DFnOzHKcsae97AHQyL1hcx86P1tmG77DgbAzmN36RZkyee}",
//        "2023-12-15 18:45:07.003Z INFO {XAPP} - {4/17:8sVdnsXhrLjk09lM7kLKab/rdnYEByWtg4E4Bl1qyxZNenkjHwp5OQtfL2BoLFlQJVjMJNhb4QibUe6JzmrubcgsdxOye0QvmNqTmmCvrTgpSd5VfSoTXbbm}",
//        " {5/17:yfYGHcXBkPCiEzXpYHLiuTs6+dzbSoA8AiECdzULwd1kMTquminuqAjSUwThfrWilB6FMD29E0KcojO9QGA6kcDshOIR1mLlUUjBuDtONO6qCrfTq3SWxNt1} ",
//        "..{6/17:B1rxuK9RwNKrELHbrXRECsl968UEqxIUzF5UHtVI2DzSQlweYlh8FH9VZXExUgIUpXcqleb+KLVLz0Jxb1DrLKluToxZerA6+cGaIcAjG2OQR1uiwMMsceAx}..",
//        "{7/17:MhKeO6isviJIwqLn7tRixGPQaJTeOR7DWjq4v8W8UMTgIAEzgQImwnLop5cE9DAqwGM3wiXvoQEyPLQBNjxORceMAQ8U8Wl33d4QFScgpN1X6ENyjtRNnzam}",
//        "{8/17:AUs8jmu+H88CyONW7KbMZ4t0MgUuHpEYnnO2cZnFkwmGoRNMJPSrkF35c3JIs1/jqY8DFWxzWME2ABbMya9Ojv0rfj9xEthcfi8K/ZTH4WPk5+Oe2zfOlbmJ}{9/17:0HcHnttG0QgmgN/x2qhqAZNW2T467pROL+n0VUnzPPKn2eL3BWl12t7uQ8976O20RqPAZxTHNJgl12BYtsG0fD+dBfTaDnCwu4EcbEPQwZzlTeSkh9cTDvtk}",
//        "{10/17:KYrdaCSG2xYYgklzd8/1jGv5ujUmGUyk2W7YywzicEb3e4v0PEf53eJuCFsR6n4EDUoV/Pi9AdiG4Muken3pzVidiZjaha4jip/eBTb7qM0J2L86JY7p6//7}   {11/17:TKgs+7C7shtFufJP7QLvdOq0YhKsGZdgTdkEc56EEegMk+YWyzjaHo0YvWbZhra1f3QyfnzQWp7476ZNhJe5J8Tki6k7sd4s9x6bT7sbVJgDwyQHYUKncG24}",
//        "{12/17:7pM4yXrB7S+kLcp0kbL4CrYor+gNrgO1sld94MKLqsYVtVrMHdReRCzitjtEVZPdy48yIq/ZLu4q0iYnDOCx0y20QD9+rtf81kWYpGwyy0TJS//dO9J6dnI/}",
//        "{13/17:L3yyd3xAtrYiX+O2X7ohJghTKdeXsaGETeXbuHQ3K9uGJbtBuVY9TYTBZPnGwtyD/uF7EjJ6ZQ4uKjU0+ypV2jNWglihHXdorDe7lVWszE7lV5LCX6rKPNm8}",
//        "{14/17:NqEk66Guaa0uh5pSwH/yQJZAc/wboN8Qewn5eRIHiykzL04q6KvQ+qiCcMO3InUNqv8MIq093X/kW0yntgK0a0zo5hmt5bmKZT3HFQzjvsEi2G3GrSWzDXiV}",
//        "{15/17:WIVhXWXjrIdVBTWPqw4pKK2e8G12STKd/bv9Jlw571fseoq8VSKpw1FFUYOh+X81DPjZo2eBnSVyEm5+ECQ0TatpU0lbhei9C8h8fTtyCsRNfWawRKh62s9V}",
//        "{16/17:WqBSN8+MlieZJZEjiaH6f7KqYceOGyQzFrzID3nZsPWgIF/TrG0F1ac2FQ1PkUvGwttVk1CHhOnNSMVqt/ZhT32iavrGZB2hEp/87PIMHN3eO//h5ftffxx+}",
//        "{17/17://Nnh1/+efzd5x8//LR49GH7m/d//PXpt4+D33/5Gw==}" };

    String[] logLines = {
        "{1/66:7V29c9xIdq9yNqxy6FzFmET198dmoyG4yxO/jqSkvbWvVBQ12tMVP3QUWXdbV/sHOHHq1OVydInvb3DixMlVOXXowJEzx36vB8AAmG6gAWKGs1yKJVDqbnQ3}",
        "{2/66:Hl7/3vt1v278cWO0+fHm9ur8bvOrF5sXd5+3P7xn25f3v9+mm1uQdzv93f30y930FrNfn6Yns+S7T1eQen71GZMZYXSb0m0iXlD5FVNfSekKwZ23P9ydf49l}",
        "{3/66:rn44//w5cSmEzOq4+Ty9Pb+7uf0C+X+7MRr9Ef7OU999+gDpdKuaePfD5ylWd5KevT453Jzl3t3cnV++u7j5gs8gEmqUJkoKZpXRMs2q+HSTl1AJlZowQ61h}",
        "{4/66:glGbklmJi8/3eRGdSA2ZgsP9cpb78dPtl7t3tze/rzSkDFGEE6a5yBu6nb6rdIgnlApCiZVMUE5Mus2KgvNOkbwXkFrqCE2INVqbeT8ubq6uFm56f//x49QJ}",
        "{5/66:Em6Y13R1c1frTD3TW9vn89vp9d3sBVzfX17OUs9vv8cG3Fsabb7c39lP36T7+DKEEIwzRrTi8IAs5RL+zn4TPoZ0DukC/lLOuJm9tNHmN+n4GBTqBdYA6YQb}",
        "{6/66:RiRRVGklNc9LHe+PD/d2XrhSSnKtKIfalFAcamRYo4JWlYQUUdxzkh6f7R2krmaD9aodZdUEfqzmUJarXZUqMW/lND1I30Az7g4K92iF9UrOoR0C5SnX3MIV}",
        "{7/66:+lfcc3Zwhs9Q9F/Mnw3z8l5DDlGMQ/+ghIb/QU3wY9z/JNSHt/wIlx+3AoOABQbB4f4vjvaeB8FyBsGXu9vp+dXsBUjPwKCVYhc399czeTBUUIbabomB5xR5}",
        "{8/66:jy/vr66LclRlNd5OP3y6OIdOuEq3aaX09TmgLL5p9lLi6OEslVwYYSWMFCGFkLtiV6TzXBiJEkrAwHNlhLC1fC5ol3RJpRRUpJgjhbTVlkClpWTQlutJ3jOx}",
        "{9/66:A/+HO6BvAmqFrFJ9Y9evXWgPc8s5XC6UhlL1VifQw1RIQJvFNFpKswBJ8/9R4RpD2cDPbi13Au1y118suVPpgYIesFAuPKtBeUG9cAVJ7Qhsg236ADMdn+z/}",
        "{10/66:6uj1mQPMCQpPFoC1m55NvjkYf+vyrNBQ5S52N8/fO52MDxvyodr05BeHLyp1O1ipaVluajOcWVTA2aiE9N/AGAeLfflpismbc6Bxw+by5nOBQukvi4wv9++d}",
        "{11/66:hXfgVKR+/HQJ/sO7j+cXAF4OC4xQIHJrLLxhJnIsqPTmbvoH55Ew46RNs3dK+A5YCTqzM4hNoAl23rHz6++n767vrzZLhgth7/rD9A8w9i6/TH/nRplLR8wd}",
        "{12/66:/XqjAXh5AHjPXuIL8QEvTyQxkoBxAjsmzCLuynbcVYnVugV3A+3UYZclkjJinNm0AkTWBXZNYhQ3iLoFhjWi7rCYK+Qi5rKHYW5eYyfMJZnmGUGZXUgTnjRf}",
        "{13/66:uWpaGFVIAx6RMOKU7g5jHQngfFN6d5QnQatBWuxTa+99ts+LtQCFe4fHGdbuSomlc1gebQKQHo+/Tk9dLgVxT0qOo4Nil5POBF24dDDid/ZOZhirEH+lhgeH}",
        "{14/66:cVqUOE5T8PlcCejqBMroPO/sm5Ojs7P9NLsbWoUSRd1v9k73Xu6nL/y5b0/Gx8d7h1/XcjGz0W0UAfQ6PTo5C2IXvC4DL4/Br77gpajRANgt4OVpaBG9hDKS}",
        "{15/66:geeoLXjj3ZxGk2hKqNbc4WgcgsmB/UYhFjGMP2PYM4ZFYNjO6+P9t+OTQz8mHL4+ODl6e5oR1jwVkt7u7Zx9s5nTWFJgE4z5V+mvsPzM/xthgfHcl5q/48Kb}",
        "{16/66:AlK7WECUC/C2GkSggMv/dY6N6cHx6d53M7oOYwBosZpT6deHe798nb6oYyNmNqKfDKBfmDQ7WAKhUWCawFRFX/yDN6xsq/PGuOVKKIZe2ZPFP76If+IR8e8Z}",
        "{17/66:vepyaMOyaunOyFat6SfMi3kOaUvkxSYh7g/X1sKAp+l20WqAF1tWZcVm/hZK2mEroD00R1adcZYlhllAcuD+VHFiF3FWxOAsBWAzjTjrGpKMEwPvXRsZwlma}",
        "{18/66:cAZVCQNVUiZUJ5wVCWC06IazYmCc5WYRZ+XDcDZH7g44a0p4YRow1gQR1rTgq2nAIOPF0FqfAqOj+f6uCGwC+FuXjxd7V46RzVx5OAQVK0BQBo5VBqHwsmz7}",
        "{19/66:xKJChQNp7OJDC14oRA6oOntly4NPHYDP774LwyfFnlMjlGXgri7CJ8/gU3ClFNHaA58yARW1pAU+oSGmrHRkPoCdKhEW/GWLJYkhMjdaUdjJEqCUWmsK4yke}",
        "{20/66:PfnQ6OnxUtXK0VOV0EE1oKcKoqdqQU/VgJ7Ki361PjWgZ/B+7/DK7+iKrCqArKrBv3w5nrxyQwkEDTcwuL0N+zZGLdzSBAbtbPIwOGaJBK8ORonxuDxRY1bA}",
        "{21/66:/frhY1YCRwWKa6SiQHhxMbbTmCUW/Z1HHrNscczqB41Z3X3IMlyCQvMNKl2eIGOlYcMCKst6qD+bL5NK6YZveRmVNQw2bA2tvtPvWXqmph8qUs2e9sPN+99O}",
        "{22/66:L+7efbn4zfTq3I0cdFXgXlArgj+b1YIol2wQlRrBjoudWkOFvJNiFHyovYoMfD8E3sWH0Mv4yQi9ilCT1yfvJkcHBz6E2p+8Gr852tvx5YFT9+Jkb8etYyDJ}",
        "{23/66:5oyXQ2DmKxkTeDw+9xhPjt7uH01e5esUxq1iSObAR3KGKxu88Hg2T1/tHb/bOzz19aBxxWP8smgFHj11ygBX8KuKNouyL/dOj/bfzIKJuERxcRQdlkXRCxxj}",
        "{24/66:k9mLLO5Z0opKDQZa/doZEo8W/NrT8cnXQ/m183iWUdCVFQx1F/VYFu6rLKX1dmG3mp6ehgIGBn58AyYNqAe1RjLd7td7ht48dOChPn2zQMxKBKLA1hBOwXfm}",
        "{25/66:hkYQHVmbKQqjWm+x4LWR6titamIRywkYNglRHXjlREr0VxjRXrdJ4IS9UMwaRo3fbQKlUaphQdK1AypGFUcFCS5IykQZIpQmYAHBDbNdHSeCAIuOWZzTxIZ2}",
        "{26/66:mrK0stNkKsXqThMVBFRHholOVuHDnCavVaytcG2MWnxySgLqFVruDr30rsqlGbfDKBfgFcgD5C40lO+kXDQxkijBpDaPp1/MLuqXXVf9Kq+v1v2aiNVV5l1b}",
        "{27/66:nS+qBrvRZb2z6Fe79tOA9jfOIwlgk0xpYzgntK/+C8lti/ZrDCQT0oDP/YS13zMJT6vlOqt/j9VOn+tToaYBu1+K72ic6aFVnlVSckxpirZtiJgt7vXPbDVF}",
        "{28/66:4pajfYV3FqrsGb/MfKHFUtJTKmZO6aH8IehB7wK3woaGcxqfgBc9vFB+ip40De0ICQcm00RIbZXQiJO+NVfWDvYMYNZqEgb7vBFFKbJ8HQJ7UEXGKKVWu4Ua}",
        "{29/66:lW6romAr2PNEEYVzA4Yzw8R8pnS1e0JY7puU4Z5WyhUwnjDgH/gHUJaRIuC9Dve5+9QB7eOxes2R2YuzezvfHr05O3BbrBZncZ5SHG/tzfe3Gqdn45zujAZA}",
        "{30/66:x7UwGZRql9F3Kurs6PipSgSvzaYiuIcFWEiToahjeHdLoZQlTcQ41lqU7UCrdSCPZQ48cyu5mX42B8OYg8lpilobgNcTwPUX48kEcxcWKvaP3h6MzwJ3Bgj5}",
        "{31/66:xqiNdId2WIRj35zOC2UI+C5cWQ/pjhpdysCLo6ZhITjUUH1wAce3gqBXpyzoTafQN5pQpjHWRsnSOG+NfdMUn48CWQdfdNBBSD0TUDkA9hyEOYPvPAqDS4ZP}",
        "{32/66:e4wuJVTNjcTae2h3kVYRcdadP4oafwwrS2evIJ4/dt8coRNtASMYoJKyVtN8+M1BgmbAhYGwmhlf0C4ugVhj4NmbtoeFmlrcHsEB2pTGKGBrVdfNrYISnUNX}",
        "{33/66:3IwhXyZ0ebaK5Wk16OIJccClGWNWK/BBq2CUl8vnI39OyNWOSTU62BuTsvymELLOiLWKXQYwasA1MpTjuR2SRzAYvohYLkgDFWUXX0DBa5aIWKFtBg0hd1Yo}",
        "{34/66:oonAE2IASRYBSyR8FiwMhE5gYFg+MsoeDgffCIQgm1f4LAgBlAMcMhiQKgRY4JNpEL4iVDCjhe4EWBIcNcMfM+bO51yFdhkEJFsFqB6rG48NLJVwuQxg+0fL}",
        "{35/66:1UZRrY2SNEORcpkEu0XKPbYM46PfmvcmtMTGFXOD8xXcpnnB56i4IecnZ3A96hIFtT9f1ImzZDTbLwdwagmlKoeZUaMl8+kocWcIvXSvW4ux2GWS6f7WbKtJ}",
        "{36/66:MnZtJbM4rqty4Q+VC16brXxoN0w4RIwnAhm8MZThWTjzCQyPQa0Ydsbw5DKqG2dRoHamuMYYYsLAyJctds20c8PwrB0BDodi3Uy7SAyQCgtCJ/FkZOjwhTz+}",
        "{37/66:vWzdQ7tgoqx7j9gdHhm70zk2LLRhI3wUSuC9t2oVMdYOqVVcAhc2SgCt1t1m59xEmxaPq1X5MXdlrQrt03hsrVpyRFiwG0uKCAuF2+59Oz7c8Ss9mHsAUq1B}",
        "{38/66:LamMV3pqCLCf5hMvAlU/PY33HFtGQ0G2URqf73TqpvEeaz6P8Kqo4maTFuXLWh4tCkeaKG41QzIMb5KzSDWSCfBsbQipxPB51jW4ARpMOIFGjGVBTVKgchrn}",
        "{39/66:BTVRnHQMKGQJBvXWO7PqJUWPJoXCaWeaRC3RePpMId5BNMnnMYd0aVZVhSXnXLQ3S7bS4ETkwoxT0a9aqxG8OZNEd97cXRbOF94sjMzMNW4TQl6uqwxgKJ6/}",
        "{40/66:v5xG1D8r1zwTMdq8v/6EdOP+0pXayZOvplfvp8W53aOC64w2iwo9gsppA9jdLxdQ6O72fjpLcczAUYOcacbPEcTOAojoCKHyTEA9LyJ6qGFmIMz+6+304f4g}",
        "{41/66:pPTbs5NxpeWSTOPZe3APU0Nw0X5aZMSRVPAJ3B/KhZKUmhaSmhHzNOJHM8In2WR1aNB2JrEx3F4H5RaMQFq22JpmPWZC6iTYvmLDayP1z8Mpnx2N0codDd/S}",
        "{42/66:42M4GvEu66yi6t71LG1wNyPr1QqdjK5yWK6LUXn+9XEwFoT07F78VNwLWlis9fIveLQZrPoXi+O1r5ncahQbCYrtsd0Lz9JB4VzES7Wv1PDa7FxkUBwfPSAS}",
        "{43/66:I4SwzB3pIeERM0M3N+g6cbFATGtCpTJqPn0zt+4a12FmH2AJuRnYkPvyjabGWhEMdoJyHHrEFJWSgLdTKhfRp/oOGsAFo+JP9h96jqw4H6s9lCAs5qqxzX2Y}",
        "{44/66:Di5HOI6lthfyYQFRPmcljwzv7a2oXc0UjFAdChqwCQv5Jbmn1MkvGU4Q8ba3svyP33hJh1v+d1LGM4Lk7IwgtN9sgsvvLjYCT7dy//JZ7efwgHp4wEYT+IZ2}",
        "{45/66:oDQyOw14CINAIVGLXdSViXXHoxlbnv/3MDtcMSaGMPhTDXmvETvNqBVcUYUf09IdiR2HJ6j3ZcXELt+PWPmGin9TSizO9jjZMBZnMdqAAqzIek7L/HL24L2x}",
        "{46/66:NGM+5U1aC90td251iDuw2IbkiR2l1Zc31u3cQxijV5i5y+lhjVuBijxP57vd/VpH0hlhvpZEQx9oxEqy7EA2Q0HVg+6UXauo6igyGTy3romDn3UUS0c2ybT7}",
        "{47/66:kCaeu7zDUSR4lTO+XWyWKeNdZ9GwGNGENWa4jcTrqDB4bebRoQ2PQR4NPhSMeKqVhecEX2qRR5d8npL7BPcJqYMuHFbL8TAK3AAD8guxZiinBVHwlxoobkvl}",
        "{48/66:vD2o82RqrYr03QbnyL7jhEIbguzsDyXSwvgJ7WXs4bvlu9XmjkXstsCqj5bFfPX30TK2GfC9VNj36jULH/uQ8SZ9GC67NIP/M+OrtYHQ7kQEV3YHPNUzNIxH}",
        "{49/66:QZOwcOarm27FNzQzjz2mVKNjtVloD2kTrRfaUhjwwL4JAEo0rQcazhrofKDWxSlU7b6WqqXQwHBpJz6PUxKcRtqCwXm8J4yW+W1B2Zw+eFHWi/4tbDzfkrSI}",
        "{50/66:9Bw0lcOdTOw0ID0Ug6HqPu1O3anWeHC7xLOt8XPsHD/KTmoNL9MKtD9+G6v2PnadVcc/dV8mXbWgdR6d+7UxPLqZNbtfz7R3QFtYkmUHixU6/WBQ2rtWxx9E}",
        "{51/66:0d6wWIbjdusoFbw22/PQDuvwmYIqMUDXKbGMCpw6jiV3QJWEskAKG3dVY+0MaSN4NWa+Alu36iwRgI3AASW4QVSZdFsWBVutukmUlEoy/JoSMYX1WLF5Z749}",
        "{52/66:A7RSrrBjiTClCKz5IRsPZnoPPxX1+bS+IOr0CahdAews8SxTyo3LaIHjsGAeE45XIRe8NgNyaDNs6OS+AGBGoTGRLYfyRKJxGWdb0bfEVFaLt56AV+bf67rO}",
        "{53/66:eDtZs+PwWGiXbaMHQTUMTUmN0vATq7M0AU1UtkFbtYUHA9YGth387pC68sRIJQxOlHAqdbcvv9KEKwL3xJ17N7gSe2KomH9r7XopcWW6II9GeISJ4QwEuk0J}",
        "{54/66:yPIEQG8R/Fwocdk3Q5yRpTjypdHl/J6S71fvV9nzq+eV/b4iD7O6e309DkHp7Nx0nzQuq+bKJ43Dm9LDk8YA4RZDsOBdYIBs7KQxN3i8X3YUQ3jyGBGGMgG+}",
        "{55/66:j2A0OHc8m4M2RnFprbGq49wx6LOq92XV9sJ3wIefZOLWJ2EtsmJKeckqPtxgPPzrJKxtBjr35PqblMUIJ2gMPxQ571rA3DR81LJXDNhyxNU2Y903Diwgpd4z}",
        "{56/66:1pW6nX0owr36R4HVOpxDXZf4L6/MGypyv9ZxSrzHbEuvCfA1235UWMjlzsasx5cCXHLvWZhBBbJG0zBRcWBhPRlycuopqMmQ8lg/LcFro1ubf1izW1wc09Bt}",
        "{57/66:DX0EiIyd+AA+q41pjIsDLFWSKHCXTXmX2EJcHMOTkjQ4ydTqUrmIuDiWaM50pB87dFwc9/ix/rm7QUMh9EIsAKbgGMQleoY7n/IJgOx9V3enP9QprX5lvNbE}",
        "{58/66:fDM6CbmfeXIn97PtEeP9mOfwtz4+z7LWrzaasCzTiE4UHVgxHiiMob5CxR4cJxJtVRjLgrUuTuKi3BWOITztu9s+LZowSWKxbGhOzj0Lv7xabnVg1nK8Rj5d}",
        "{59/66:upS4LsFtQ1xXA6z1iuuKe/w2lvzwuK7KU/dlyT7D8BzdNfhU9tKgviTLeAq7kuAulsjZKdLuI50RnrjC3TllbayxE71MdrKKwK61kwhem5lJBpDdmAmMC8up}",
        "{60/66:pWUK0cZMdGIpaWYmmioAPwIGl5U+nrPITDRXFuefCbDibsxEJFyJWGs+NDMRuSUuWXP/oRZhY95jSt0uWDM7P40BV3lwVrZYmMTSno8/+Ey+yI1ob85SOxIi}",
        "{61/66:YN7RBUtzQz6Efe///PHW7JnQdLJy+T2PQWj6nD9hGLWKG0WlEUpFEhqVwChsIjSBWusYaBKqrJYWP2rG3d7QDoSGJcZET84MTWhEllYhNKxSrh0DexAaLwa2}",
        "{62/66:EJpsKXpZhAbeW5DQLAPxIp5/FYym9Ni9GY3XbDxzmifOaVZyToNJcgfeWm3newBHQRc+H1q5y26CR131deK3+kllOFqzjkLBazOz6XUWAfSYCWO1Vsr7CVO/}",
        "{63/66:WSe0kddQrXGIAm0iwcBo5DXUGqaUNZxUAqgjeA1LhOI20qgPzms8H/7ratPzDawdbPp8NXNu1WY7o8bOs6+dQFfeM1VOg1IOlEopE+ewA4560qjPVxAP/tJf}",
        "{64/66:hXIEvAKWqEG/87c6YcUbzGfa1MmQ5vc8Bm3qs7+fGc6UMDAY8EjTSNokEsmaWFOg0sWNgLikrowhTFPNeSfWZBOMNeA5HK2WNHlWtHPrFg2wPUhTEGCbiZMI}",
        "{65/66:L2UPQpzAuwgSpwaI7MWb4kWwCu5UevK+3MlnaJ6Z0xNnTiuJZ+SJEYpKbo0FL4GJ9sWP+eAqwtM4waElOHgTfYnBVj9JDMeW1kUQeHUMaQN+/bjxN3/3j3d/}",
        "{66/66:/u//+N9//fv//L8/nf/lt3/1z3/5h3//tz9f/NPZ//zwL3/9p//6fw==}" };

    LogResistantFormatter f = new LogResistantFormatter();
    String plan = f.parse(logLines);
    System.out.println("Plan: " + plan);

  }

}
