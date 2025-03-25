<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class UsrController extends Controller
{
    public function index()
    {
        return response()->json([
            'status' => 'success',
            'message' => 'Countries retrieved successfully',
            'data' => Usuario::all()
        ], 200);
    }
}
