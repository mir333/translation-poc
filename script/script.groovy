import StringPool
import JSONArray
import JSONFactoryUtil
import JSONObject
import User
import ServiceContext
import ServiceContextFactory
import UserLocalServiceUtil

// to be configured according to target system
long creatorUserId  = 414033L;
long companyId = 20116;
Locale locale = Locale.ENGLISH;
String password = "P@ssw0rd1";
long[] roleIds = [471713L];

// users json
String json = "[{\"name\":\"TestZZZ\",\"surname\":\"TestZZZ\",\"email\":\"test@wwwxxxww.com\"}]";

// default data
boolean autoScreenName = true;
long facebookId = 0L;
String screenName = null;
String openId = StringPool.BLANK;
int prefixId = 0;
int suffixId = 0;
boolean male = true;
int birthdayDay = 1;
int birthdayMonth = 1;
int birthdayYear = 1970;
long[] organizationIds = null
long[] groupIds = null;
long[] userGroupIds = null;
boolean sendEmail = false;
ServiceContext serviceContext = new ServiceContext()
JSONArray users = JSONFactoryUtil.createJSONArray(json)

for (u in users) {
    JSONObject user = u as JSONObject;
    out.println(user.getString("name") + " " + user.getString("surname"));
    def newUser = UserLocalServiceUtil.addUserWithWorkflow(creatorUserId, companyId, false, password, password, autoScreenName, screenName,
            user.getString("email"), facebookId, openId, locale, user.getString("name"), null, user.getString("surname"), prefixId, suffixId, male,
            birthdayMonth, birthdayDay, birthdayYear, null, groupIds, organizationIds, roleIds,
            userGroupIds, sendEmail, serviceContext);

    def newUser = UserLocalServiceUtil.addUserWithWorkflow(creatorUserId, companyId, false, password, password, autoScreenName, screenName,
            user.getString("email"),locale, user.getString("name"), null, user.getString("surname"), prefixId, suffixId, male,
            birthdayMonth, birthdayDay, birthdayYear, null, groupIds, organizationIds, roleIds,
            userGroupIds, sendEmail, serviceContext);
    out.println(newUser.getUserId());
}
