
var cacheName = 'Maptrips-cache-v1.0.0';
var appShellFiles = [
    '/',
    '/css/style.css',
    '/js/main.js',
    '/assets/logo.png'
];

self.addEventListener("activate", Event => Handle_activate(Event));
self.addEventListener("fetch", Event => Handle_fetch(Event));
self.addEventListener("install", Event => Handle_install(Event));

function Handle_activate(Event)
{
    console.log(arguments.callee.toString().match(/function ([^\(]+)/)[1] + ' ' + Event);
}

function Handle_fetch(Event)
{
    console.log(arguments.callee.toString().match(/function ([^\(]+)/)[1] + ' ' + Event);
}

function Handle_install(Event)
{
    Event.waitUntil(
        caches.open(cacheName).then((cache) => {
            return cache.addAll(appShellFiles);
        })
    );
    console.log(arguments.callee.toString().match(/function ([^\(]+)/)[1] + ' ' + Event);
}
