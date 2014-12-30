/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
/**
 * 
 */
package org.ednovo.gooru.server.request;

/**
 * @author Search Team
 * 
 */
public enum UrlToken {

	GET_USER("/v2/user/{0}?sessionToken={1}"),

	GET_USERBYTOKEN("/usertoken/user?sessionToken={0}"),

	GUEST_SIGNIN("/account/signin.json?isGuestUser=true&apiKey={0}"),

	SIGNIN("/account/signin.json?apiKey={0}&sessionToken={1}"),

	RESOURCE_SEARCH(
			"/search/resource?sessionToken={0}&query={1}&pageNum={2}&pageSize={3}&queryType={4}&allowDuplicates={5}&fetchHitsInMulti={6}&allowScripting={7}"),

	COLLECTION_SEARCH(
			"/search/collection?sessionToken={0}&query={1}&pageNum={2}&pageSize={3}&accessType={4}"),

	SIMPLE_COLLECTION_SEARCH(
			"/search/scollection?sessionToken={0}&query={1}&pageNum={2}&pageSize={3}&accessType={4}"),

	SELF_STUDY("/taxonomy/course.json?sessionToken={0}"),

	FEATURED_COLLECTION("/featured/theme?sessionToken={0}&limit={1}"),

	RESOURCE_INSTANCE_LIST("/scollection/{0}?sessionToken={1}"),

	COLLECTION_LEARN_MORE(
			"/scollection/resource/moreinfo?sessionToken={0}&resourceId={1}"),

	RESOURCE_COLLECTION_LIST(
			"/search/scollection?sessionToken={0}&pageNum={1}&pageSize={2}&flt.resourceGooruOIds={3}&boostField.hasNoThumbnail=0"),

	COLLECTION_RESOURCE_LIST(
			"/search/resource?sessionToken={0}&pageNum={1}&pageSize={2}&flt.collectionGooruOIds={3}"),

	COLLECTION_ITEMS_LIST("/scollection/{0}?sessionToken={1}"),

	SEARCH_FILTER("/search/index/filters?sessionToken={0}&type={1}"),

	SEARCH_SUGGEST_QUERY(
			"/search/searchquery?sessionToken={0}&query={1}&pageSize={2}&pageNum={3}"),

	SEARCH_SUGGEST_STANDARD("/search/{0}?sessionToken={1}&query={2}&pageSize={3}&pageNum={4}"),
	
	SUGGEST_STANDARD_BY_FILTER("/v2/collection/standards?sessionToken={0}&query={1}"),
	
	SEARCH_AUTO_SUGGEST_KEYWORD(
					"/search/searchquery?sessionToken={0}&query={1}&pageSize={2}&queryType={3}&pageNum={4}"),

	SEARCH_SUGGEST_SOURCE(
			"/search/publisher?sessionToken={0}&query={1}&pageSize={2}&pageNum={3}"),

	CREATE_COLLLECTION("/scollection?sessionToken={0}"),

	UPDATE_COLLLECTION("/scollection/{0}?sessionToken={1}"),
	
	UPDATE_V2_COLLLECTION("/v2/collection/{0}?sessionToken={1}"),

	DELETE_COLLECTION("/v2/collection/{0}?sessionToken={1}"),
	
	/*CREATE_COLLECTION_ITEM(
			"/scollection/item?sessionToken={0}&resourceId={1}&collectionId={2}"),*/
	
	V2_CREATE_COLLECTION_ITEM(
			"/v2/collection/{0}/item?sessionToken={1}"),
			
	UPDATE_COLLECTION_ITEM("/scollection/item/{0}?sessionToken={1}"),

	/*DELETE_COLLECTION_ITEM("/scollection/item/{0}?sessionToken={1}"),*/
	
	V2_DELETE_COLLECTION_ITEM("/v2/collection/item/{0}?sessionToken={1}"),

	SHARE_SHORTEN_URL("/url/shorten/{0}?sessionToken={1}"),
	
	SHARE_SHORTEN_URL_PLAY("/url/shorten/{0}?sessionToken={1}&realUrl={2}"),
	
	/*REORDER_COLLECTION_ITEM(
			"/scollection/item/{0}/reorder?sessionToken={1}&newSequence={2}"),
*/
	V2_REORDER_COLLECTION_ITEM_SEQUENCE(
			"/v2/collection/item/{0}/reorder/{1}?sessionToken={2}"),

	COPY_COLLECTION(
			"/scollection/{0}/copy?sessionToken={1}&skipCollectionItem={2}&addToShelf={3}"),

	GET_COLLECTION_ITEMS(
			"/scollection/{0}/item?sessionToken={1}&pageSize={2}&pageNo={3}"),

	/*GET_COLLECTION(
			"/scollection/{0}?sessionToken={1}&skipCollectionItem={2}&includeMetaInfo=true&merge=permissions"),*/
		
	V2_GET_COLLECTION(
					"/v2/collection/{0}?sessionToken={1}&skipCollectionItem={2}&includeMetaInfo=true&merge=permissions"),
					
	V2_GET_COLLECTIONForStandards(
					"/v2/collection/{0}?sessionToken={1}&rootNodeId={2}&skipCollectionItem={3}&includeMetaInfo=true&merge=permissions"),
					
	GET_TREE("/taxonomy/{0}/tree.json?sessionToken={1}"),

	LIST_COLLECTION(
			"/scollection/list?sessionToken={0}&pageSize={1}&pageSize={2}&sCollection={3}"),

	TAXONOMY_COURSE("/taxonomy/course.json?sessionToken={0}"),

	USER_COLLECTION("/myshelf?format={0}&sessionToken={1}&filterBy=collection&merge=permissions"),
	
	SHARABLE_USER_COLLECTION("/myshelf?format={0}&sessionToken={1}&sharing=public,anyonewithlink&filterBy=collection&merge=permissions"),
	
	/*USER_AVAILABILITY(
			"/user/check/availability.json?type={0}&keyword={1}&sessionToken={2}"),*/
	
	V2_USER_AVAILABILITY(
			"/v2/user/{0}/availability?sessionToken={1}&keyword={2}"),
			
	REGISTER_USER("/user?sessionToken={0}"),

	GET_REGISTERED_USER_DETAILS("/user/{0}/registration?sessionToken={1}"),

	GET_USER_PROFILE_DETAILS("/user/{0}/profile.json?sessionToken={1}"),
	
	GET_USER_PROFILE_V2_DETAILS("/v2/user/{0}?sessionToken={1}"),
	
	GET_USER_PROFILE_PAGE("/v2/party/{0}/custom-field/show_profile_page?sessionToken={1}"),

	UPDATE_USER_PROFILE_DETAILS(
			"/user/{0}/profile/personal.json?sessionToken={1}"),
	
	UPDATE_USER_PROFILE_VISIBILTY("/v2/party/{0}/custom-field?sessionToken={1}"),	
	
	UPDATE_USER_BIOGRAPHY("/v2/user/{0}?sessionToken={1}"),

	DELETE_USER_BIOGRAPHY("/v2/user/{0}/meta?sessionToken={1}"),

	UPDATE_USER_GRADE_COURSE("/v2/user/{0}?sessionToken={1}"),

	UPDATE_COLLLECTION_METADATA("/scollection/{0}/metadata?sessionToken={1}"),

	UPDATE_COLLLECTION_ITEM_METADATA(
			"/scollection/item/{0}/metadata?sessionToken={1}"),
			
	GET_COLLLECTION_ITEM(
			"/v2/collection/item/{0}?sessionToken={1}&includeAdditionalInfo=true"),

	UPDATE_REGISTER_USER("/user/{0}?sessionToken={1}"),

	SIGNOUT("/account/signout.json?sessionToken={0}"),

	GET_COLLABORATOR("/scollection/getCollaborators/{0}?sessionToken={1}"),

	ADD_COLLABORATOR(
			"/scollection/addCollaborators/{0}?collaborator={1}&sessionToken={2}"),

	DELETE_COLLABORATOR(
			"/scollection/deleteCollaborators/{0}?collaborator={1}&sessionToken={2}"),

	SEND_CONFIRMATION_MAIL("/user/register/confirm/mail.json?sessionToken={0}"),

	/*COPY_COLLLECTION_ITEM(
			"/scollection/item/{0}/copy?sessionToken={1}&collectionId={2}"),
*/			
	V2_COPY_COLLLECTION_ITEM(
					"/v2/collection/item/{0}/copy/{1}?sessionToken={2}"),
	FORGOT_PASSWORD("/user/password/reset.json?sessionToken={0}&emailId={1}"),

//	RESET_CREDENTIAL(
//			"/user/reset/credential.json?sessionToken={0}&password={1}&token={2}"),
	V2_RESET_CREDENTIAL(
					"/v2/user/reset-password?sessionToken={0}"),

	MEDIA_FILE_UPLOAD(
			"/v2/media?sessionToken={0}"),

	MEDIA_FILE_SAVE("/resource/{0}/media?sessionToken={1}&mediaFileName={2}"),

	IMAGE_CROP(
			"/media/{0}/crop?sessionToken={1}&height={2}&width={3}&xPosition={4}&yPosition={5}&&cropEngine=bufferImage"),

	UPDATE_USER_VIEW("/user/{0}/view/flag?sessionToken={1}&viewFlag={2}"),

	GET_USER_COLLECTIONS("/scollection/list?sessionToken={0}&fetchType=my"),

	ADD_NEW_RESOURCE(
			"/v2/collection/{0}/resource?sessionToken={1}&title={2}&url={3}&category={4}&description={5}&thumbnailImgSrc={6}&stop={7}"),

	GET_RESOURCE_INFO(
			"/resource/suggest/meta/info?sessionToken={0}&url={1}&title=Nothing&fetchThumbnail=true"),

	CHECK_RESOURCE_EXISTS(
			"/resource/search.json?url={0}&checkShortenedUrl=true&sessionToken={1}"),

	CREATE_FOLDER("/folder?sessionToken={0}"),

	LIST_MY_FOLDERS("/folder/my/workspace?sessionToken={0}&pageSize={1}&orderBy={2}"),

	LIST_MY_FOLDER_LEVELS("/folder/{0}/item?sessionToken={1}"),
	
	LIST_MY_FOLDERS_COLLECTIONS("/folder/{0}?sessionToken={1}"),

	GET_A_FOLDER_INFORMATION("/folder/{0}?sessionToken={1}"),

	CREATE_CLASSPAGE("/classpage?sessionToken={0}"),
	
	CREATE_CLASSPAGE_V2("/v2/class?sessionToken={0}"),
	
	CREATE_CLASSPAGE_ITEM_V2("/v2/classpage/{0}/item?sessionToken={1}"),
	
	ASSIGN_COLLECTION_OR_FOLDER_TO_CLASS_V2("/v2/class/{0}/assign/{1}?sessionToken={2}"),
	
	GET_CLASSPAGE_ITEMS_V2("/v2/classpage/{0}/item?sessionToken={1}&offset={2}&limit={3}"),
	
	UPDATE_CLASSPAGE_ITEMS_V2("/v2/class/item/{0}?sessionToken={1}"),
	
	DELETE_CLASSPAGE_ITEMS_V2("/v2/classpage/item/{0}?sessionToken={1}"),
	
	GET_COLLECTION_PARENT_FOLDERS("/v2/collection/{0}/parents?sessionToken={1}"),
	
	// LIST_MY_CLASSPAGES("/classpage/list?sessionToken={0}"),
	LIST_MY_CLASSPAGES("/classpage/my?sessionToken={0}"),

	GET_CLASSPAGE_BY_CODE("/classpage/code/{0}?sessionToken={1}"),

	GET_CLASSPAGE_BY_ID("/classpage/{0}?sessionToken={1}&includeCollectionItem=true"),

	ADD_QUESTION_ITEM("/scollection/{0}/question?sessionToken={1}"),

	UPDATE_QUESTION_ITEM("/assessment-question/{0}.json?sessionToken={1}"),

	CLASSPAGE_DELETE("/classpage/{0}?sessionToken={1}"),

	UPDATE_QUESTION_IMAGE(
			"/quiz-question/{0}/media?sessionToken={1}&mediaFileName={2}&assetKey=asset-question"),

	REMOVE_QUESTION_IMAGE("/quiz-question/{0}/asset?sessionToken={1}"),

	ATTACH_IMAGE_TO_QUESTION(
			"/quiz-question/{0}/asset?sessionToken={1}&fileNames={2}&assetKey=asset-question"),

	CREATE_ASSIGNMENT(
			"/assignment?sessionToken={0}&classpageId={1}&assignmentDueDate={2}"),

	UPDATE_ASSIGNMENT("/assignment/{0}?sessionToken={1}&assignmentDueDate={2}"),

	DELETE_ASSIGNMENT("/assignment/{0}?sessionToken={1}"),

	/*UPDATE_RESOURCE_INFO(
			"/resource/{0}.json?sessionToken={1}&resourceTitle={2}&description={3}&category={4}&mediaFileName={5}"),*/
	UPDATE_RESOURCE_INFO(
			"/v2/collection/resource/{0}?sessionToken={1}"),
			
	UPDATE_RESOURCE_INFO_NO_MEDIA(
			"/resource/{0}.json?sessionToken={1}&resourceTitle={2}&description={3}&category={4}"),
	
	ADD_COLLECTIONS_TO_ASSIGNMENT("/assignment/item?resourceId={0}&assignmentId={1}&sessionToken={2}"),
	
	GET_ASSIGNMENT_COLLECTIONS("/assignment/{0}/item?sessionToken={1}"),
	
	DELETE_ASSIGNMENT_COLLECION_ITEM("/assignment/item/{0}?sessionToken={1}"),
	
	UPDATE_USER("/user/{0}?sessionToken={1}&format=json&username={2}&userrole={3}"),

//	GET_CLASSPAGE_ASSIGNMENTS("/classpage/{0}/item?sessionToken={1}");
	
	GET_CLASSPAGE_ASSIGNMENTS("/classpage/{0}/item?sessionToken={1}&pageSize={2}&pageNum={3}"),
	
	V2_GET_CLASSPAGE_TITLES("/v2/classpage/collection/{0}?sessionToken={1}"),
	
	UPDATE_CLASSPAGE("/classpage/{0}?sessionToken={1}"),
	
	PERMISSION_COLLECTION("/user/content/{0}/check-access.json?sessionToken={1}"),
	
	V2_CREATE_CLASSPAGE("/v2/class?sessionToken={0}"),
	
	V2_UPDATE_CLASSPAGE("/v2/class/{0}?sessionToken={1}"),
	
	V2_SOCIAL_SHARE("/v2/share/mail?sessionToken={0}"),
	
	V2_LIST_MY_CLASSPAGES("/v2/classpage/my?sessionToken={0}&limit={1}&offset={2}"),
	
	V2_GET_CLASSPAGE_ASSIGNMENTS("/v2/classpage/{0}/item?sessionToken={1}&data={%22limit%22:%22{2}%22,%22offset%22:%22{3}%22}"),
	
	V2_GET_LISTTEACHCLASSES("/v2/class/my/teach?sessionToken={0}&limit={1}&offset={2}&randomId={3}"),
	
	V2_GET_LISTSTUDYCLASSES("/v2/class/my/study?sessionToken={0}&limit={1}&offset={2}&randomId={3}"),
	
	V2_CLASSPAGE_DELETE("/v2/class/{0}?sessionToken={1}"),
	
	V2_CREATE_ASSIGNMENT("/v2/assignment?sessionToken={0}"),
	
	V2_GET_CLASSPAGE_BY_CODE("/v2/class/code/{0}?sessionToken={1}"),
	
	V2_GET_CLASSPAGE_BY_ID("/v2/class/{0}?sessionToken={1}&merge=permissions"),
	
	V2_UPDATE_ASSIGNMENT("/v2/assignment/{0}?sessionToken={1}"),
	
	V2_DELETE_ASSIGNMENT("/v2/assignment/{0}?sessionToken={1}"),
	
	V2_DELETE_COLLECTION("/v2/classpage/collection/{0}?sessionToken={1}"),
	
	V2_ADD_COLLECTIONS_TO_ASSIGNMENT("/v2/assignment/{0}/item?sessionToken={1}"),
	
	V2_GET_ASSIGNMENT_COLLECTIONS("/v2/assignment/{0}/item?sessionToken={1}&data={%22skipPagination%22:%22yes%22}&sharing=public,anyonewithlink"),
	
	V2_DELETE_ASSIGNMENT_COLLECION_ITEM("/v2/assignment/{0}/item/{1}?sessionToken={2}"), 
	
	GET_PROFILE_WORKSPACE("/scollection/{0}/workspace?sessionToken={1}&pageNum={2}&pageSize={3}"),
	
	GENERATE_BITLY_LINK("/v2/share/url/shorten?sessionToken={0}"),
	
	UPLOAD_PROFILE_IMAGE("/user/{0}/profile/picture?sessionToken={1}&mediaFileName={2}"),
	
	PROFILE_PAGE_EVENT("/activity/log/{0}/profile?sessionToken={1}"), 
	
	CHECK_SHORTEN_URL("/resource/search.json?url={0}&feature=player_embedded&checkShortenedUrl=true&sessionToken={1}"),
	
	V2_ADD_QUESTION_ITEM("/v2/collection/{0}/question?sessionToken={1}"),
	
	V2_FEATURED_THEME_COLLECTIONS("/v2/theme/{0}?sessionToken={1}"),
	
	CHECK_CLASSPAGE_PERMISSIONS_BY_ID("/user/content/{0}/check-access.json?sessionToken={1}"),
	
	V2_CREATE_USER("/v2/user?sessionToken={0}"),
	
	V2_SIGNIN("/v2/account/login?apiKey={0}"),
	
	V2_SIGNOUT("/v2/account/logout?sessionToken={0}"),
	
	V2_GUEST_SIGNIN("/v2/account/loginas/anonymous?apiKey={0}"),
	
	V2_GET_USER_BY_SESSIONTOKEN("/v2/user/token/{0}?sessionToken={1}"),

	V2_ADD_NEW_USER_RESOURCE("/v2/collection/{0}/resource?sessionToken={1}"),

	V2_JOIN_CLASS("/v2/class/{0}/member/join?sessionToken={1}"),
	
	V2_UPDATE_USER_RESOURCE("/v2/collection/resource/{0}?sessionToken={1}"),
	
	V2_USER_RESOURCE_MEDIA_FILE_SAVE("/v2/media?sessionToken={0}"),
	
	//V2_CREATE_USER("/v2/user?sessionToken={0}"),
	
	SEARCH_SUGGEST_NO_RESULT(
			"/suggest/resource?sessionToken={0}&context={1}"),
			
	SEARCH_SUGGEST_COLLECTION_NO_RESULT(
			"/suggest/scollection?sessionToken={0}&context={1}"),
	UPDATE_VIEW_COUNT("/resource/update/views/{0}.json?sessionToken={1}"),
	
    START_ACTIVITY_LOG("/activity/log/{0}/start?sessionToken={1}"),
	
    STOP_ACTIVITY_LOG("/activity/log/{0}/stop?sessionToken={1}"),
    
    CREATE_SESSION("/v2/session?sessionToken={0}"),
    
    UPDATE_SESSION("/v2/session/{0}?sessionToken={1}"),
    
    GET_SESSION("/v2/session/{0}?sessionToken={1}"),
    
    CREATE_SESSION_ITEM("/v2/session/{0}/item?sessionToken={1}"),
    
    CREATE_SESSION_ITEM_ATTEMPT("/v2/session/{0}/item/{1}/attempt?sessionToken={2}"),
    
    V2_SOCIAL_EMAIL("/v2/share/mail?sessionToken={0}"),
    
    GET_USER_PROFILE("/v2/party/{0}/custom-field/show_profile_page?sessionToken={1}"),
    
    GET_RESOURCE_DETAILS("/resource/{0}/play.json?sessionToken={1}"),
    
    V2_GET_RESOURCE_DETAILS("/v2/resource/{0}?sessionToken={1}"),
	
	COLLECTION_PLAY_EMBEDED_URL("embed/collection.htm?id={0}"),
	
	COLLECTION_PLAY_URL("%23preview-play%26id={0}"),
	
	V2_GET_LIBRARY_FEATURED_USERS("/v2/library/user/contributors?sessionToken={0}"),

    V2_COPY_COLLECTION("/v2/collection/{0}/copy?sessionToken={1}"),
    
    COPY_RENAME_COLLECTION("/scollection/{0}/copy?sessionToken={1}&addToShelf={2}&title={3}"),
    
    V2_COPY_COLLECTION_ITEM("/v2/collection/item/{0}/copy/{1}?sessionToken={2}"),
    
    COPY_RESOURCCE("/scollection/item?sessionToken={0}"),
    
    V2_GET_USER_WORKSPACE("/v2/collection/{0}/workspace?sessionToken={1}&offset={2}&limit={3}"),
    
    GET_USER_WORKSPACE("/myshelf?format=json&sessionToken={0}&pageNum={1}&pageSize={2}"),
    
	V2_CONTENT_THUMBS_RATING("/v2/rating/{0}?sessionToken={1}"),
	
	CONTENT_THUMBS_RATING("//content/{0}/rating.json?sessionToken={1}"),
	
	GET_CONTENT_REPORT("/v2/content/{0}/report?sessionToken={1}&creatorUId={2}"),
	
	CREATE_CONTENT_REPORT("/v2/report?sessionToken={0}"),
	
	UPDATE_CONTENT_REPORT("/v2/report/{0}?sessionToken={1}"),
	
	DELETE_CONTENT_REPORT("/v2/report/{0}?sessionToken={1}"),
	
	SIMPLE_COLL_GETAPI("/v2/collection/{0}?sessionToken={1}&includeCollectionItem=false&includeMetaInfo=false&includeCollaborator=false&includeRelatedContent=false"),
	
	V2_GET_LIBRARY_COURSES("/v2/library/{0}?sessionToken={1}"),
	
	V2_GET_LIBRARY_TOPIC_OFFSET("/v2/library/{0}/{1}?sessionToken={2}&offset={3}&limit={4}"),
	
	V2_UPDATE_USER_PROFILE("/v2/user/{0}?sessionToken={1}"),
		
	GET_LIBRARY_CONCEPT("/scollection/{0}?sessionToken={1}&requestContext=library"),

	V2_GET_LIBRARY_UNIT_OFFSET("/v2/library/{0}/unit/{1}?sessionToken={2}&offset={3}&limit={4}"),
	
	V2_GET_COLLECTION_COMMENTS("/v2/comment?sessionToken={0}&gooruOid={1}&offset={2}&limit={3}"),
	
	V2_CREATE_COLLECTION_COMMENT("/v2/comment?sessionToken={0}"),
	
	V2_DELETE_COLLECTION_COMMENT("/v2/comment/{0}?sessionToken={1}"),
	
	V2_UPDATE_COLLECTION_COMMENT("/v2/comment/{0}?sessionToken={1}"),
	
	FILE_UPLOAD_GET_URL("/media?uploadFileName={0}&imageURL=&sessionToken={1}"),
	
	CREATE_RECTION("/v2/reaction?sessionToken={0}"),
	
	GET_CONTENT_REACTION("/v2/content/{0}/reaction?sessionToken={1}&creatorUId={2}"),
	
	DELETE_CONTENT_REACTION("/v2/reaction/{0}?sessionToken={1}"),
			
	PROFANITY_CHECK("/v2/profanity?sessionToken={0}"),
	
	V2_GENERATE_PDF("/v2/media/htmltopdf?sessionToken={0}"),
	
	V2_SEND_EMAIL_WITH_PDF("/v2/share/mail?sessionToken={0}"),
	
	GET_FEATURED_COLLECTIONS("/v2/theme/featured?sessionToken={0}"),
	
	V2_GET_LIBRARY_COLLECTIONS("/v2/library/{0}/collection/{1}?sessionToken={2}"),
	
	V2_SEND_WELCOME_MAIL("/v2/user/sendmail/{0}?sessionToken={1}"),
	
	POPULAR_COLLECTION("/search/scollection?sessionToken={0}&pageNum={1}&pageSize={2}&flt.course={3}"),
	
	V2_GET_COLLABORATORS("/v2/collaborator/content/{0}?filterBy={1}&sessionToken={2}&groupByStatus=true"),
	
	V2_COLLABORATORS("/v2/collaborator/content/{0}?sessionToken={1}"),
	
	V2_SUGGEST_COLLAB("/v2/collaborator/suggest?query={0}&sessionToken={1}"),
	 	
	V2_WORKSPACE_FOLDER_LIST("/v2/folder/my/workspace?sessionToken={0}&offset={1}&limit={2}&orderBy=sequence"),
	
	V2_GET_CHILD_FOLDER_LIST("/v2/folder/{0}/item?sessionToken={1}&offset={2}&limit={3}&orderBy=sequence"),
	
	V2_GET_CHILD_FOLDER_LIST_PUBLIC("/v2/folder/{0}/item?sessionToken={1}&offset={2}&limit={3}&sharing=public"),
	
	V2_DELETE_COLLABORATORS("/v2/collaborator/content/{0}?sessionToken={1}&data={2}"),
	
	V2_LIST_CLASSPAGES_BY_USER_ID("/v2/classpage/collection/{0}?gooruUId={1}&sessionToken={2}"),
	
	V2_CREATE_FOLDER("/v2/folder?sessionToken={0}"),

	V2_DELETE_FOLDER("/v2/folder/{0}?sessionToken={1}"),
	
	V2_MOVE_COLLECTION("/v2/folder/move?sessionToken={0}"), 
	
	V2_GET_POPULAR_LIBRARY("/v2/library/{0}/collection/popular?sessionToken={1}&libraryName=popular&pageNum=1&pageSize=5"),
	
	V2_CREATE_COLLECTION_IN_FOLDER("/v2/collection?sessionToken={0}"),
	
	V2_UPDATE_FOLDER_METADATA("/v2/folder/{0}?sessionToken={1}"),
	
	V2_COPY_COLLECTION_IN_FOLDER("/v2/collection/{0}/copy?sessionToken={1}"),
	
	V2_COLLECTION_USED_COUNT("/v2/classpage/collection/{0}/count?sessionToken={1}"),
	
	V2_GET_COLLECTION_RESOURCE_LIST("/v2/folder/{0}/item?sessionToken={1}&orderBy=sequence"),
	
	V2_INVITE_STUDENT_TO_CLASS("/v2/invite/class/{0}?sessionToken={1}"),
	
	V2_DELETE_MEMBER_FROM_CLASS_BY_CODE("/v2/class/{0}/member/remove?sessionToken={1}"),
	
	V2_GET_MEMBER_LIST_BY_CODE("/v2/class/{0}/member?sessionToken={1}&filterBy={2}&limit={3}&offset={4}"),
	
	V2_SUGGEST_MEMBER("/v2/class/member/suggest?query={0}&sessionToken={1}"),
	
	V2_GET_CLASSPAGE_COLL_DETAILS("/v2/classpage/item/{0}?sessionToken={1}"),
	
	V2_PARTNER_WORKSPACE("/v2/folder/{0}/workspace?sessionToken={1}&fetchChilds=true&itemLimit={2}&offset={3}&limit={4}&topLevelCollectionType=folder"),
	
	V2_PROFILE_WORKSPACE("/v2/folder/{0}/workspace?sessionToken={1}&fetchChilds=true&itemLimit={2}&offset={3}&limit={4}"),
	
	GET_CLASSPARTY_CUSTOMFIELD("/v2/party/{0}/custom-field/classpage_welcome_popup_is_autoopen?sessionToken={1}"),
	
	V2_UPDATE_PARTY_CUSTOM_FIELD("/v2/party/{0}/custom-field?sessionToken={1}"),
	
	V2_PARTNER_CHILD_FOLDER_LIST("/v2/folder/{0}/item?sessionToken={1}&fetchChilds=true&&itemLimit={2}&offset={3}"),
	
	V2_GET_PARTNERS("/v2/partner?sessionToken={0}"),
	
	CREATE_STAR_RATINGS("/v2/rating?sessionToken={0}"),
	
	GET_STAR_RATINGS("/v2/rating/{0}?sessionToken={1}&creatorUId={2}"),
	
	GET_CONTENT_STAR_RATINGS("/v2/content/{0}/rating/star/count?sessionToken={1}"),
	
	UPDATE_STAR_RATINGS("/v2/rating/{0}?sessionToken={1}"),
	
	GET_USER_STAR_RATINGS("/v2/user/{0}/rating/star/count?sessionToken={1}"),
	
	GET_USER_RATINGS_REVIEWS("/v2/content/{0}/rating/star?sessionToken={1}&offset={2}&limit={3}"),
	
	DELETE_TAXONOMY_RESOURCE("/v2/resource/{0}/taxonomy?sessionToken={1}"),
	
	UPDATE_TAXONOMY_RESOURCE("/v2/resource/{0}?sessionToken={1}"),
	
	ADD_TAGS("/v2/content/{0}/tag?sessionToken={1}"),
	
	GET_TAGS("/v2/content/{0}/tag?sessionToken={1}"),
	
	DELETE_TAGS("/v2/content/{0}/tag?sessionToken={1}&data={2}"),
	
	TEACH_STUDY("/v2/class/my/teach-study?sessionToken={0}&limit={1}&offset={2}"),
	
	SEARCH_SUGGEST_AGGREGATOR("/search/aggregator?sessionToken={0}&query={1}&pageSize={2}&pageNum={3}"),
			
	GET_LOGGED_IN_USER_RATINGS_REVIEWS("/v2/content/{0}/rating/star?sessionToken={1}&creatorUId={2}"),
	
	DELETE_RATINGS("/v2/rating/{0}?sessionToken={1}"),
	
	SEARCH_SUGGEST_RESOURCES("/gooru-search/rest/suggest/v2/resource?sessionToken={0}&searchTerm={1}&event={2}&contentGooruOid={3}"),

	USER_FOLLOWERS("/v2/user/{0}/followers?sessionToken={1}&offset={2}&limit={3}"),
	
	USER_FOLLOWING("/v2/user/{0}/following?sessionToken={1}&offset={2}&limit={3}"),
	
	USER_FOLLOW("/v2/user/follow/{0}?sessionToken={1}"),
	
	USER_UNFOLLOW("/v2/user/unfollow/{0}?sessionToken={1}"),
	
	USER_IS_FOLLOW("/v2/user/{0}/isfollow?sessionToken={1}"),
	
	USER_TAG("/v2/content/tag/{0}?sessionToken={1}&offset={2}&limit={3}"),
	
	USER_TAG_RESOURCE("/v2/tag/{0}/resource?sessionToken={1}&offset={2}&limit={3}&gooruUId={4}"),
	
	UPDATE_ASSIGNMENT_SEQUENCE("/v2/class/item/{0}/reorder/{1}?sessionToken={2}"),
	
	GET_SAUSD_LIBRARY("/v2/folder/{0}/workspace?sessionToken={1}&fetchChilds=true&itemLimit={2}&offset={3}&limit={4}&&topLevelCollectionType=folder"),
	
	GET_STANDARD_LIBRARY_MENUS("/v2/library/{0}/item?sessionToken={1}"),
	
	SERVER_STATUS_URL("http://status.goorulearning.org/api/v1/services/gooru-production-api"),
	
	GET_GOOGLEDRIVE_FIlES("/v2/files?maxResults=20&q={0}"),
	
	GET_COURSE_DETAILS_STANDARDS("/v2/library/{0}/item/course/{1}?sessionToken={2}"),
	
	UPDATE_FILE_PERMISSION("/v2/files/{0}/permissions"),
	
	V2_GET_LIBRARY_SUBJECTS_OPTIMIZED("/v2/library?sessionToken={0}&libraryName={1}"),
	
	V2_GET_LIBRARY_COURSES_OPTIMIZED("/v2/library/{0}/item?sessionToken={1}&libraryName={2}"),
	
	V2_GET_LIBRARY_UNITS_OPTIMIZED("/v2/library/{0}/item/course/{1}?sessionToken={2}"),
	
	V2_GET_LIBRARY_TOPICS_OPTIMIZED("/v2/library/{0}/item/unit/{1}?sessionToken={2}&offset={3}&limit={4}"),
	
	V2_GET_LIBRARY_LESSONS_OPTIMIZED("/v2/library/{0}/item/topic/{1}?sessionToken={2}&offset={3}&limit={4}"),
	
	V2_LevelWiseStandards("/v2/standard/{0}/{1}?sessionToken={2}"),
	
	REFRESH_TOKEN("/gooru-auth/google/new/token.g?refreshToken={0}"),
	
	ASSIGN_ITEM_TO_CLASS("/v2/class/{0}/assign/{1}?sessionToken={2}&direction={3}&planedEndDate={4}"),
	
	SUGGEST_STANDARD_BY_FILTER_Source_CodeId("/search/standard?sessionToken={0}&query={1}"),
	
	REFRESH_TOKEN_GDC("/gooru-auth/google/refresh-token.g?partyUid={0}"),
	
	REVOKE_TOKEN_GD("/gooru-auth/google/revoke-token.g?partyUid={0}"),
	
	GET_COLLECTION_SUMMARY("/insights/api/v1/classpage/{0}.json?sessionToken={1}&data={2}"),

	RESET_TOKEN_EXPIRE("/v2/user/check-reset-token?sessionToken={0}&resetToken={1}"),

	V2_UPDATE_QUESTION_ITEM("/v2/collection/question/{0}?sessionToken={1}"),

	V2_REORDER_FOLDER_COLLECTION("/v2/folder/item/{0}/reorder/{1}?sessionToken={2}"),

	V2_USER_PUBLISHEDCOLLECTIONS_COUNT("/insights/api/v2/query?sessionToken={0}&data={1}"),

	RESOURCE_TAGS("/v2/content/resource/tag/{0}?sessionToken={1}&offset={2}&limit={3}"),
	
	V1_COLLECTIONPROGRESSDATA("/v1/classpage/{0}/users/usage.json?sessionToken={1}&data={2}"),
	
V1_GETUSERSFORPATHWAY("/v1/classpage/{0}/users.json?sessionToken={1}&data={2}"),
	
	V1_GETCOLLECTIONMETADATA("/v1/classpage/{0}.json?sessionToken={1}&data={2}"),
	
	V1_GETCOLLECTIONRESOURCEDATA("/v1/classpage/{0}/resources.json?sessionToken={1}&data={2}"),
	
	V1_GETSESSIONSDATABYUSER("/v1/classpage/{0}/sessions.json?sessionToken={1}&data={2}"),
	
	V1_GETSESSIONDATABYUSERSESSION("/v1/classpage/{0}/resources.json?sessionToken={1}&data={2}"),
	
	V2_ITEMFEEDBACK("/v2/session/{0}/item/feedback?sessionToken={1}"),
	
	V1_EXPORTSUMMARYATHWAY("/v1/classpage/{0}/summary/export.xls?sessionToken={1}&data={2}&timeZone={3}"),
	
	V2_PATHWAY_ITEM_MOVE_WITH_REORDER("/v2/class/{0}/pathway/{1}/item/{2}/move?sessionToken={3}"),
	
	V2_GET_ASSIGNMENT_PARENT_DETAILS("/v2/class/assignment/{0}?sessionToken={1}"),
	
	V1_GETGRADEJSON("/v1/classpage/{0}/grade.json?sessionToken={1}&data={2}"),
	
	V1_EXPORTOEPATHWAY("/v1/classpage/{0}/oe/export.xls?sessionToken={1}&data={2}&timeZone={3}"),
	
	V1_OETEXTJSON("/v1/classpage/{0}/OEText.json?sessionToken={1}&data={2}"),
	
	V2_DOWNLOADFILE("/v2/media/download?sessionToken={0}&url={1}&filename={2}");
	

	private String url;

	private UrlToken(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
