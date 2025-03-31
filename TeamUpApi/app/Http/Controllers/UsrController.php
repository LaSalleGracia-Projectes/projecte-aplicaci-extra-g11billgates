<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class UsrController extends Controller
{
    public function index()
    {
        return response()->json([
            'status' => 'success',
            'message' => 'Users retrieved successfully',
            'data' => Usuario::all()
        ], 200);
    }
}
