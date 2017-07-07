# POC-LDIP
Proof OF Concept - Live Data Insert {and} Push

Usefull tutorial for: How to create a dynamic maven project and execute "maven clean install" with skip tests:
- [https://crunchify.com/how-to-create-dynamic-web-project-using-maven-in-eclipse/]

Technologies used:

- Javax Servlet 3.0
- Apache Commons FileUpload
- Apache Commons IO
- Youtube Data API v3
- Google O'Auth Client Jetty

To generate Permanent access token for facebook:

Put a GET request to this: https://graph.facebook.com/v2.9/oauth/access_token?grant_type=fb_exchange_token&client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&fb_exchange_token=SHORT_LIVED_ACCESS_TOKEN
