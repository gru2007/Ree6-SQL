package de.presti.ree6.sql.util;

import de.presti.ree6.sql.entities.Setting;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage the Settings for the SQL-Worker.
 */
public class SettingsManager {

    /**
     * The List of all Settings.
     */
    @Getter(AccessLevel.PUBLIC)
    private static List<Setting> settings;

    /**
     * Constructor called to add all settings to the list.
     */
    public static void loadDefaults() {
        if (settings == null)
            settings = new ArrayList<>();

        settings.clear();
        // TODO:: create an structure. (configuration_,logging_,message_)
        settings.addAll(List.of(
                new Setting("-1", "chatprefix", "Prefix", "ree!"),
                new Setting("-1", "level_message", "Send Levelup Message", false),
                new Setting("-1", "configuration_news", "Receive News", true),
                new Setting("-1", "configuration_moder_records", "Moder download records", false),
                new Setting("-1", "data_last_report", "Last Report", "1"),
                new Setting("-1", "configuration_ban_server", "Ban sync server", "-1"),
                new Setting("-1", "configuration_language", "Language", "en-GB"),
                new Setting("-1", "message_join", "Welcome Message", "Welcome %user_mention%!\nWe wish you a great stay on %guild_name%"),
                new Setting("-1", "message_rules_1", "Глава 1", "**1)** Ваш ник должен соответствовать определенной форме:\n"
                        +        "**Для клонов** Отряд/Звание/Номер/Позывной - **Пример:** 91 SSG 4986 Apache | 91 PVT 3545 (__Позывной необязателен, если у вас его нету__).\n"
                        +        "**Для джедаев** Ранг/Звание/Имя/Фамилия - **Пример:** JM GEN Omadari Stelmil | JK Tens Kroft \n"
                        +        "**Для независимых** Имя/Фамилия - **Пример:** Pre Vizsla | Aga Awaud (__Клан необязательный, если у вас его нету__)\n"
                        +        "Позывной не должен совпадать с одним из нынешних участников проекта, "
                        +        "быть рофельным, иметь какой-то шутливый смысл или обозначение.\n"
                        +        "**2)** Модерация дискорда имеет право поменять ваш ник на: 'Ник по форме 1/3"
                        +        "Ник по форме 2/3, Ник по форме 3/3', если ваш ник не соответствует положенной форме.\n"
                        +        "**3)** Если вы получили 'Ник по форме 3/3', то вы можете получить предупреждение "
                        +        "(<@&996707186946363554>), также с вас будут сняты все роли (кроме наказаний) и "
                        +        "выдана роль <@&1009496063314493450>.\n\n"
                        +        "**Примечание:** На игроков с ролью гость данная глава правил не распространяется."),
                new Setting("-1", "message_rules_2", "Глава 2", "**1)** Запрещено оскорбление участников сервера, его родственников, ставить смайлики и реакции оскорбительного характера.\n\n"
                        +                "```yaml\nНаказание: мут на 2ч. За рецидив Strike```\n\n"
                        +                "**2)** Запрещен флуд в любых его проявлениях: Текст/эмодзи/гифки/картинки/стикеры и тд. **Искл.**<#996153353673388043>\n\n"
                        +                "```yaml\nНаказание: мут на 1ч. За рецидив Strike```\n\n"
                        +                "**3)** Запрещена публикация NSFW-контента.\n\n"
                        +                "```yaml\nНаказание: мут на 1ч. За рецидив Strike```\n\n"
                        +                "**4)** Запрещено провоцировать участников Discord-сообщества на оскорбления или выяснения отношений.\n\n"
                        +                "```yaml\nНаказание: мут на 3ч. За рецидив Strike```\n\n"
                        +                "5) Запрещено разжигание межнациональной и расовой розни, межрелигиозной розни, политической розни.\n\n"
                        +                "```yaml\nНаказание: мут на 3ч. За рецидив Strike```\n\n"
                        +                "6) Запрещено заниматься оффтопом в любом канале. Исключение: <#996153266763223100> и <#996153353673388043>.\n\n"
                        +                "```yaml\nНаказание: мут на 1ч. За рецидив Strike```\n\n"
                        +                "7) Запрещено обсуждение политических тем, а именно: обсуждение политических идеологий, новостей связанных с политической повесткой, политических фигур и т.д.\n"
                        +                "```yaml\nНаказание: мут на 2ч. За рецидив Strike```\n\n"
                        +                "8) Запрещено беспричинное и частое упоминание ролей/участников сервера.\n\n"
                        +                "```yaml\nНаказание: мут на 1ч. За рецидив Strike```\n\n"),
                new Setting("-1", "message_rules_3", "Глава 3", "**1)** Запрещена реклама сторонних проектов/ресурсов, любая деятельность, направленная на получение коммерческой выгоды.\n\n"
                        +                "```yaml\nНаказание: Ban```\n\n"
                        +                "**2)** Запрещено распространение личных данных других участников проекта без их явно выраженного согласия.\n\n"
                        +                "```yaml\nНаказание: мут на 2ч. За рецидив Strike```\n\n"
                        +                "**3)** Запрещено распространять контент, который может нарушить работу Discrod-a, либо пользоваться уязвимостями платформы в личных целях.\n\n"
                        +                "```yaml\nНаказание: Strike/Ban (На усмотрение модерации).```\n\n"
                        +                "**4)** Запрещён любой шок-контент! Информационные материалы содержащие угрозы, сцены насилия, вызывающие отвращение, или имеющие откровенно непристойный характер. Запрещены элементы порнографии, насилия, пропаганды терроризма, неонацизма, нацизма, фашизма, коммунизма, алкогольной продукции, наркотических средств и т.п.\n"
                        +                "```yaml\nНаказание: мут на 24ч. За рецидив Strike```\n\n"
                        +                "**5)** Запрещено препятствовать работе администрации проекта.\n\n"
                        +                "```yaml\nНаказание: мут на 30 минут. За рецидив Strike```\n\n"
                        +                "**6)** Запрещено использование своего положения (должности), для получения, личной, выгоды.\n\n"
                        +                "```yaml\nНаказание: мут на 3ч. За рецидив Strike/Снятие```\n\n"
                        +                "**7)** Запрещено покидать дискорд/пользоваться твинк аккаунтом с целью обхода наказания.\n\n"
                        +                "```yaml\nНаказание: Strike/Ban (На усмотрение модерации)```\n\n"),
                new Setting("-1", "message_rules_child", "Дочерние дискорды", "**1)** Дочерние дискорды обязаны соблюдать правила площадки дискорд и правила основного дискорда сервера.\n\n"
                        +"**2)** 2) Дочерние дискорды являются частичной автономией, а это значит что они могут дополнительно расширять перечень правил в своем дискорде, развивать его.\n\n"
                        +"**3)** Выходя из 2 пункта, модерация основного дискорда проекта, не модерирует дочерние дискорды, однако она обязана выдавать наказания игрокам проекта, если на них пришла жалоба.\n\n"
                        +"**4)** Для выдачи наказания, пользователю дочернего дискорда, модерация должна связаться с ответственным за дискорд и сообщить о нарушителе. Ответственный за дискорд обязуется принять меры которые предписывают правила основного дискорда. После чего модератор пишет отчет в соответствующем канале.\n\n"
                        +"**5)** В случаи, если ответственный за дочерний дискорд, не соблюдает предписанные требования, официальная модерация проекта может выдать ряд предупреждений, при вынесении третьего предупреждения дискорд лишается частичной автономии и полностью переходит под контроль модерации проекта. Восстоновление автономии происходит в частном формате между представителем данного дискорда и старшей модерацией проекта.\n\n"
                        +"**6)** В случаи нарушения правил пользователь получает мут только в дочернем дискорде, однако, если был предписан страйк или Ban, пользователь их получает, во всех дискордах проекта."),
                new Setting("-1", "message_rules_admin", "Административные каналы", "**1)** Административные каналы в дискорде подчиняются правилам дискорда.\n"
                        +"**2)** Модерация проекта оставляет за собой право регулировать наказание на свое усмотрение.\n"
                        +"**3)** Степень регулировки: минимальное наказание **предупреждение** максимальное согласно правилам дискорда.\n"
                        +"**4)** В случаи если ивентолог будут использовать голосовые каналы Event Voice №1 , Event Voice №2, Event Voice №3 не для проведения ивентов, а для своих личных собраний, трансляций сторонних игр и т.д., модерация проекта имеет право отсоединить ивентолога с данных каналов и вынести наказание на свое усмотрение.\n **Степень регулировки:** минимальное наказание предупреждение, максимальное наказание Strike ( Мут не более 30 минут )\n"
                        +"**5)** Пояснение правил дискорда 3.5: администратор имеет право попросить покинуть голосовой канал, прекратить писать в ветках и других административных каналах у любого пользователя дискорда, где тот или иной администратор имеет приоритет"),
                new Setting("-1", "message_rules_other", "Примечание", "**1)** Профили сервера также подвергаются модерации и подчиняются правилам сервера (Аватарки, шапки, обо мне) ( Правило было введено в связи с большим количеством жалоб от игроков )\n\n"
                        +"**2)** При оценке ситуации связанной с нарушениями или возможными нарушениям правил, модерация их оценивает в частном порядке. В случаи если пользователь недоволен вынесенным решением модерации он может его оспорить в установленном порядке, а именно подать апелляцию, в крайнем случаи, может прибегнуть к жалобе на администрацию проекта.\n"
                        +"**3)** 2.7 - пояснение: дискорды проекта это в первую очередь подспорье в игре на сервере и его неотъемлемая часть. У нас играют разные люди и коллективы, в связи с этим, любое **Обсуждение и пропаганда** политических тем запрещена на сервере. А это значит, что любая публикация которая так или иначе затрагивает политические темы может быть удалена, а вы привлечены к ответственности согласно данному пункту.\n"
                        +"**4)** При получении дискорд бана, у пользователей снимаются все уникальные роли, после возвращения уникальные роли не будут восстановлены.\n\n"

                        +"```yaml\nСвод правил будет дополнятся и изменятся с течением времени```"),
                new Setting("-1", "message_join_image", "Welcome Image", ""),
                new Setting("-1", "logging_invite", "Log Invites", true),
                new Setting("-1", "logging_memberjoin", "Log Member joins", true),
                new Setting("-1", "logging_memberleave", "Log Member leave", true),
                new Setting("-1", "logging_memberban", "Log Member ban", true),
                new Setting("-1", "logging_memberunban", "Log Member unban", true),
                new Setting("-1", "logging_nickname", "Log Member Nickname change", true),
                new Setting("-1", "logging_voicejoin", "Log Member voice channel join", true),
                new Setting("-1", "logging_voicemove", "Log Member voice channel move", true),
                new Setting("-1", "logging_voiceleave", "Log Member voice channel leave", true),
                new Setting("-1", "logging_roleadd", "Log Member role receive", true),
                new Setting("-1", "logging_roleremove", "Log Member role removed", true),
                new Setting("-1", "logging_voicechannel", "Log voice channel changes", true),
                new Setting("-1", "logging_textchannel", "Log text channel changes", true),
                new Setting("-1", "logging_rolecreate", "Log role creation", true),
                new Setting("-1", "logging_roledelete", "Log role deletion", true),
                new Setting("-1", "logging_rolename", "Log role name changing", true),
                new Setting("-1", "logging_rolemention", "Log role mention changing", true),
                new Setting("-1", "logging_rolehoisted", "Log role hoisted changing", true),
                new Setting("-1", "logging_rolepermission", "Log role permission changing", true),
                new Setting("-1", "logging_rolecolor", "Log role color changing", true),
                new Setting("-1", "logging_messagedelete", "Log message delete", true),
                new Setting("-1", "logging_timeout", "Log Member timeouts", true),
                new Setting("-1", "configuration_rewards_blackjack_win", "Payment Amount on BlackJack win", 200.0),
                new Setting("-1", "configuration_rewards_musicquiz_win", "Payment Amount on Music Quiz win", 200.0),
                new Setting("-1", "configuration_rewards_musicquiz_feature", "Payment Amount on Music Quiz Feature guess", 100.0),
                new Setting("-1", "configuration_rewards_musicquiz_artist", "Payment Amount on Music Quiz Feature guess", 50.0),
                new Setting("-1", "configuration_rewards_musicquiz_title", "Payment Amount on Music Quiz Title guess", 25.0),
                new Setting("-1", "configuration_work_min", "Minimum received Payment for work", 10.0),
                new Setting("-1", "configuration_work_max", "Maximum received Payment for work", 50.0),
                new Setting("-1", "configuration_work_delay", "Delay between each work", 5L),
                new Setting("-1", "configuration_steal_delay", "Delay between each steal", 5L),
                new Setting("-1", "message_ticket_menu", "Message that should display in the Ticket Menu.", "By clicking on the Button below you can open a Ticket!"),
                new Setting("-1", "message_ticket_open", "Message that should display when a Ticket is opened.", "Welcome to your Ticket!"),
                new Setting("-1", "message_suggestion_menu", "Message that should display in the Suggestion Menu.", "Suggest something"),
                new Setting("-1", "configuration_autopublish", "Automatically publish News messages.", false)
                ));
    }

    /**
     * Retrieve the default Setting by name.
     *
     * @param settingName The name of the Setting.
     * @return The Setting.
     */
    public static Setting getDefault(String settingName) {
        return settings.stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null);
    }

    /**
     * Retrieve the default value of the Setting by name.
     * @param settingName The name of the Setting.
     * @return The value of the Setting.
     */
    public static Object getDefaultValue(String settingName) {
        return getDefault(settingName).getValue();
    }

    /**
     * Retrieve the default display name of the Setting by name.
     * @param settingName The name of the Setting.
     * @return The display name of the Setting.
     */
    public static String getDefaultDisplayName(String settingName) {
        return getDefault(settingName).getDisplayName();
    }
}
