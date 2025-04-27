<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;
use App\Models\Chat;
use App\Models\MatchUsers;

class EnsureUserInChatMatch
{
    public function handle(Request $request, Closure $next)
    {
        $idChat = $request->route('idChat');

        $chat = Chat::find($idChat);

        if (!$chat) {
            return response()->json(['error' => 'Chat not found'], 404);
        }

        $match = MatchUsers::find($chat->IDMatch);

        if (!$match) {
            return response()->json(['error' => 'Match not found'], 404);
        }

        $userId = auth()->id();

        if ($match->IDUsuario1 != $userId && $match->IDUsuario2 != $userId) {
            return response()->json(['error' => 'Forbidden: you are not part of this chat'], 403);
        }

        return $next($request);
    }
}
