package LangForU_Development.LangForU.email;

import org.springframework.stereotype.Service;
@Service
public class EmailTemplateService {

    public String buildEmail_AdminActivation(String name, String link) {
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#4255A4;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#4255A4";
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
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\">Кликни тук за потвърждение</a></p>\n" +
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
        String style = "font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#4255A4;background-color:#ffffff";
        String headerStyle = "font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block";
        String bodyStyle = "font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:1.5;color:#4255A4";
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
                "          <p style=\"Margin:0 0 20px 0;font-size:16px;line-height:25px\"><a href=\"" + link + "\" style=\"color:#1D70B8;text-decoration:none;\">Кликни тук за потвърждение</a></p>\n" +
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


}
