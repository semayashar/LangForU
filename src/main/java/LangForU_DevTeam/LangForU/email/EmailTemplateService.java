package LangForU_DevTeam.LangForU.email;

import org.springframework.stereotype.Service;


@Service
public class EmailTemplateService {


    public String buildEmail_AdminActivation(String name, String link) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Активация на Администраторски Права</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Уважаеми/а " + name + ",</p>\n" +
                "          <p>Благодарим ви, че се съгласихте да станете администратор на нашата платформа. За да завършите процеса на активация на администраторските права, моля, кликнете на следния линк:</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\"> &#8680; <u> Кликни тук за потвърждение</u> &#8678; </a></p>\n" +
                "          <p>Обърнете внимание, че този линк ще изтече след 15 минути.</p>\n" +
                "          <p>Очакваме с нетърпение да започнем съвместната ни работа!</p>\n" +
                "          <p>С уважение,<br>Вашият екип</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_Registration(String name, String link) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Потвърдете Регистрацията Си</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Здравейте, " + name + ",</p>\n" +
                "          <p>Благодарим ви за регистрацията в LangForU! За да завършите регистрационния процес и да активирате акаунта си, моля, кликнете на следния линк:</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\"> &#8680; <u> Кликни тук за потвърждение</u> &#8678; </a></p>\n" +
                "          <p>Моля, обърнете внимание, че този линк ще изтече след 15 минути.</p>\n" +
                "          <p>Очакваме с нетърпение да ви приветстваме!</p>\n" +
                "          <p>С уважение,<br>Екипът на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_LectureReleased(String name, String lectionName) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">🎉 Нова лекция е публикувана! 🎉</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Здравей, " + name + "! 👋</p>\n" +
                "          <p>С радост ти съобщаваме, че новата лекция е вече налична: <strong>" + lectionName + "</strong>! 📚✨</p>\n" +
                "          <p>Не забравяй да я разгледаш и да се насладиш на обучението!</p>\n" +
                "          <p>С най-добри пожелания,<br>Екипът на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_AccountDeletion(String name, String email) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Изтриване на Акаунт</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Уважаеми/а " + name + ",</p>\n" +
                "          <p>Искаме да Ви уведомим, че съгласно Вашето искане, или поради други основателни причини, акаунтът Ви в LangForU- 2024 с имейл " + email + " е изтрит днес.</p>\n" +
                "          <p>С изтриването на акаунта Ви ще бъдат премахнати всички свързани с него лични данни и няма да имате достъп до предоставяните от нас услуги.</p>\n" +
                "          <p>Ако имате въпроси или се нуждаете от допълнителна информация, моля, не се колебайте да се свържете с нас.</p>\n" +
                "          <p>С уважение,<br>Екипът на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }


    public String buildEmail_AccountDisable(String name, String email) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Деактивация на Акаунт</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Уважаеми/а " + name + ",</p>\n" +
                "          <p>Искаме да Ви уведомим, че съгласно Вашето искане, или поради други основателни причини, акаунтът Ви в LangForU- 2024 с имейл " + email + " е деактивиран днес.</p>\n" +
                "          <p>С деактивирането на акаунта Ви личните Ви данни ще бъдет запазени до момента в който си активирате акаунта отново.</p>\n" +
                "          <p>Ако имате въпроси или се нуждаете от допълнителна информация, моля, не се колебайте да се свържете с нас.</p>\n" +
                "          <p>С уважение,<br>Екипът на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_AccountEnabled(String name, String email) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Активация на Акаунт</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Уважаеми/а " + name + ",</p>\n" +
                "          <p>С радост Ви уведомяваме, че акаунтът Ви в LangForU-2024 с имейл " + email + " беше успешно активиран.</p>\n" +
                "          <p>Можете вече да влезете в системата с Вашите потребителски данни и да продължите да използвате услугите ни.</p>\n" +
                "          <p>Ако имате въпроси или се нуждаете от допълнителна помощ, не се колебайте да се свържете с нас.</p>\n" +
                "          <p>С уважение,<br>Екипът на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_contactAnswer(String middleContent, String adminName, String userName) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">Отговор на запитване</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>Уважаеми/а " + userName + ",</p>\n" +
                "          <p>" + middleContent + "</p>\n" +
                "          <p>Ако имате въпроси или се нуждаете от допълнителна информация, моля, не се колебайте да се свържете с нас.</p>\n" +
                "          <p>С уважение,<br>" + adminName + "<br>Екип на LangForU</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          LangForU DevTeam - 2024\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_NewLectionNotification(String name, String courseName, String lectionTitle, String link) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">📚 Нова Лекция 📚</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>👋 Здравейте, " + name + "!</p>\n" +
                "          <p>🎉 С удоволствие ви уведомяваме, че нова лекция '<strong>" + lectionTitle + "</strong>' вече е достъпна в курса '<strong>" + courseName + "</strong>'! 🌟</p>\n" +
                "          <p>📖 Можете да кликнете на следния линк, за да разгледате съдържанието:</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\"> ➡️ <u> Виж лекцията тук</u> ⬅️ </a></p>\n" +
                "          <p>💭 Очакваме с нетърпение да чуем вашето мнение! 😊</p>\n" +
                "          <p>Поздрави,<br>LangForU Team 🌈</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          🌍 LangForU DevTeam - 2024 🌍\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmail_NewBlogNotification(String name, String blogTitle, String link) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#7270db;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#7270db";
        String headerGradient = "background:linear-gradient(90deg,#6f42c1,#e83e8c);";
        String footerStyle = "font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#ffffff;text-align:center;padding: 13px 13px 13px 10px;";
        String footerBackground = "background:linear-gradient(to right, #ff512f, #f09819);";

        return "<div style=\"" + style + "\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"60\" style=\"" + headerGradient + "\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:600px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:10px;\">\n" +
                "                  <span style=\"" + headerStyle + "\">📰 Нов Блог Пост 📰</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:600px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td style=\"" + bodyStyle + "\" align=\"left\">\n" +
                "          <p>👋 Здравейте, " + name + "!</p>\n" +
                "          <p>🌟 Радваме се да ви уведомим за нашия нов блог пост: '<strong>" + blogTitle + "</strong>'! 🎉</p>\n" +
                "          <p>Можете да кликнете на следния линк, за да го прочетете:</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\"> ➡️ <u> Прочетете блога тук</u> ⬅️ </a></p>\n" +
                "          <p>💭 Ще се радваме да споделите вашето мнение! 😊</p>\n" +
                "          <p>Поздрави,<br>LangForU Team 🌈</p>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"" + footerBackground + "; " + footerStyle + "\">\n" +
                "          🌍 LangForU DevTeam - 2024 🌍\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

}